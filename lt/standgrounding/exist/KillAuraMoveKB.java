package lt.standgrounding.exist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;

import net.minecraft.server.v1_11_R1.EntityPlayer;
@SuppressWarnings("unused")
class KillAuraMoveKB implements Listener{
	private Plugin p;	
	KillAuraMoveKB(Exist plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		p = plugin;
	}
	private double Pitch1 = 0;
	private double Pitch2 = 0;
	private double Yaw1 = 0;
	private double Yaw2 = 0;
	List<Integer> timestamp = new ArrayList<Integer>();
	List<Integer> timestate = new ArrayList<Integer>();
	List<Integer> timelist = new ArrayList<Integer>();
	List<Long> tickMap = new ArrayList<Long>();
	private boolean combat = false;
	protected double MaxReach = 3.4;
	protected LivingEntity LE;
	protected boolean H;
	protected byte TPS;
	protected boolean AntiKB = true;
	private boolean AntiAngularKillAura = true;
	private boolean CrosshairFF = true;
	private byte AV = 30;
	public AngularVelocity initAV(){
		AngularVelocity Rsp = new AngularVelocity();
		Rsp.setDeltaYaw(Yaw1, Yaw2);
		Rsp.setDeltaPitch(Pitch1, Pitch2);
		Rsp.stamp();
		return Rsp;
	}
	//daug kerta vienu metu
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(CrosshairFF && !checkForLag() && e.getDamager().getType() == EntityType.PLAYER && hasMeleeItem((Player) e.getDamager())){
			if(getTarget((Player) e.getDamager()) != e.getEntity()){
				Punishment.apply((Player) e.getDamager(), Punishment.BAN, Reason.KILLAURAA_CROSSHAIR);
			}
			if(combat==false){
				((Player) e.getDamager()).sendMessage(ChatColor.YELLOW+"[Exist]"+ChatColor.DARK_AQUA+"Kovojate! Antièytas tikrina jûsø veiksmus.");
				combat=true;
				recordTimeState();
				int DeltaT = compare(timestate);
				if(DeltaT == -1){
					//kai DeltaT yra -1, nieko nedarome
				} else if(DeltaT >= 0 && DeltaT < 5 && !checkForLag()){
					System.out.println(ChatColor.YELLOW+"[Exist]"+ChatColor.DARK_AQUA+"Kirtote dviems objektams vienu metu arba tam paèiam objektui per greitai! BANIUKAS PAREINA :^) ("+e.getDamager().getName()+")");
					Punishment.apply((Player) e.getDamager(), Punishment.BAN, Reason.KILLAURA_MULTIHIT);
				}
	  			Bukkit.getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("Exist"), new Runnable(){
					@Override
					public void run() {
						combat=false;
						((Player) e.getDamager()).sendMessage(ChatColor.YELLOW+"[Exist]"+ChatColor.DARK_AQUA+"Nustojote kovoti");
					}
	  			}, 80L);
			}
		}
		
		if(e.getEntity().getType() == EntityType.PLAYER){
		Player antiKBPlayer = (Player) e.getEntity();
		Location loc0 = antiKBPlayer.getLocation();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("Exist"), new Runnable(){
			@Override
			public void run(){
				Location loc1 = antiKBPlayer.getLocation();
				if(hasNoSurroundingBlocks(antiKBPlayer))
				antiKBPlayer.sendMessage("You have no surrounding blocks");
				if(checkForLag()){
					antiKBPlayer.sendMessage("server has unhealthy tickrate and won't ban you");
				}
				if(loc0 == loc1 && hasNoSurroundingBlocks(antiKBPlayer) && !checkForLag()){
					Punishment.apply(antiKBPlayer, Punishment.BAN, Reason.KILLAURA_KNOCKBACK);
				}
			}
		}, 5L);
		}
	}
	protected boolean hasNoSurroundingBlocks(Player antiKBPlayer) {
		Location Q = antiKBPlayer.getLocation();
		Location[] QArray = new Location[8];
		for(Location O : QArray){
			O = new Location(antiKBPlayer.getWorld(), Q.getX(), Q.getY(), Q.getZ());
		}
		//masyvas vietoms aplink zaideja
		if(QArray[0] == null) System.out.println("element 0 is null");
		if(QArray == null) System.out.println("Array is null");
		if(Q == null) System.out.println("Q is NULL");
		QArray[0].setX(Q.getX()+1); //deltaX = 1, deltaY = 0, deltaZ = 0
		QArray[1].setX(Q.getX()-1);
		QArray[2].setY(Q.getY()+2); //deltaY=+2
		QArray[3].setZ(Q.getZ()+1);
		QArray[4].setX(Q.getX()+1); //4
		QArray[4].setY(Q.getY()+1); //4 X+1 Y+1
		QArray[5].setX(Q.getX()-1);
		QArray[5].setY(Q.getY()+1);
		QArray[6].setZ(Q.getZ()+1);
		QArray[6].setY(Q.getY()+1);
		QArray[7].setZ(Q.getZ()-1);
		QArray[7].setY(Q.getY()+1);
		QArray[8].setZ(Q.getZ()-1);
		//funkcija
		for(Location loc : QArray){
			if(loc.getBlock().getType() != Material.AIR){
				return false;
			}
		}
		return true;
	}
	//auto aim
	@EventHandler
	public void onMove(PlayerMoveEvent q){
			Location v1 = q.getFrom();
			Location v2 = q.getTo();
			boolean T = false;
			//flyhax/movehax
			T = movecheck(v1, v2);
			if(T && q.getPlayer().getGameMode() == GameMode.SURVIVAL){
				Punishment.apply(q.getPlayer(), Punishment.BAN, Reason.TP);
			}
			//killaura kampinis greitis
			Pitch1 = (double) v1.getPitch();
			Yaw1 = (double) v1.getYaw();
			Pitch2 = (double) v2.getPitch();
			Yaw2 = (double) v2.getYaw();
			if(combat){
				AngularVelocity N = initAV();
				int A;
				if (timestamp != null) {
					A = timestamp.size();
				} else {
					A = 0;
				}
				timestamp.add(N.destamp());
				if(compare(timestamp) > 0){
					N.recordingTimeframe(compare(timestamp));
					if (check(N) && checkForLag() == false){
						Punishment.apply(q.getPlayer(), Punishment.BAN, Reason.KILLAURA_ANGULARVELOCITY);
					}
				}
			}
	}
	private boolean movecheck(Location loc1, Location loc2) {
		timelist.add((int) System.currentTimeMillis());
		int Q = compare(timelist);
		double maxgreitis = 8.578;
		double flatdistance, distance;
		if(Q >= 0 && Q < 50) {
			flatdistance = Math.sqrt(Math.pow(loc1.getX() - loc2.getBlockX() , 2)+Math.pow(loc1.getZ() - loc2.getZ(), 2));
			if(flatdistance > maxgreitis){
				return true;
				//ban = true
			}
		} return false;
	}
	private int compare(List<Integer> H) {
		int A = H.size();
		int B;
		if(A >= 2) B = H.get(A-1) - H.get(A-2);
		else B = -1;
		return B;
	}
	private boolean check(AngularVelocity w){	
		double edge = (double) (int) AV;
		if(w.getSum() <= edge) return false;
		else return true;
	}
	private boolean hasMeleeItem(Player p){
		Material[] Q = {Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_SPADE, Material.DIAMOND_SWORD,
				Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_SPADE,
				Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_PICKAXE,
				Material.GOLD_AXE, Material.GOLD_PICKAXE, Material.GOLD_SPADE, Material.GOLD_SWORD};
		//skaitome, kad ItemStack[pos] paruosti
		for(Material M: Q){
			ItemStack N = new ItemStack(M);
			if(p.getInventory().getItemInMainHand() == N || p.getInventory().getItemInOffHand() == N){
				return true;
			}
			N = null;
		}
		return false;
	}
	protected Location getApproxLocation(Location l){
		//i = 100, gali buti visur
		// i = 0 - tik pagal linija
		
		return l;
	}
	private void recordTimeState(){
		timestate.add((int) System.currentTimeMillis());
	}
	public boolean checkForLag(){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tickMap.add(System.currentTimeMillis());
				if(tickMap.size() > 2){
					int dT = (int) (tickMap.get(tickMap.size()-1) - tickMap.get(tickMap.size() - 2));
					boolean val;
					//1000 ms = 20 tick; 1 tick = 50 ms
					if(dT > (int) TPS){
						val = true;
					} else {
						val = false;
					}
					H = val;
				}
			}
		}, 1);
		return H;
	}
	private LivingEntity getTarget(Player A) {
        List<Entity> nearbyE = A.getNearbyEntities(MaxReach,
                MaxReach, MaxReach);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        LE = null;
        BlockIterator bItr = new BlockIterator(A);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
                block = bItr.next();
                bx = block.getX();
                by = block.getY();
                bz = block.getZ();
                        // check for entities near this block in the line of sight
                        for (LivingEntity e : livingE) {
                                loc = e.getLocation();
                                ex = loc.getX();
                                ey = loc.getY();
                                ez = loc.getZ();
                                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                                        // entity is close enough, set target and stop
                                        LE = e;
                                        return e;
                                }
                        }
                }
        		return null;
            }
	public void PassTPS(int aTPS){
		TPS = (byte) aTPS;
	}
	public void PassAntiKB(boolean insert){
		AntiKB = insert;
	}
	public void PassKillAuraAngular(boolean insert){
		AntiAngularKillAura = insert;
	}
	public void LookFF(boolean insert){
		CrosshairFF = insert;
	}
	public void passAV(int int1) {
		// TODO Auto-generated method stub
		AV  = (byte) int1;
	}
}
