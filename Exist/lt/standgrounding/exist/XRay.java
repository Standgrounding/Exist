package lt.standgrounding.exist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class XRay implements Listener {
	//private Plugin p; for schedulers
	private HashMap<Player, Set<Block>> MineralVein = new HashMap<Player, Set<Block>>();
	private HashMap<Player, Set<Set<Block>>> MineralVeins = new HashMap<Player, Set<Set<Block>>>();
	private HashMap<Player, Byte> Flag = new HashMap<Player, Byte>();
	private HashMap<Player, List<Location>> MiningLoc = new HashMap<Player, List<Location>>();
	private Set<Block> loc3 = new HashSet<Block>();
	//Set<Block> blocks = new HashSet<Block>();
	private Material[] ComparisonMaterial = {Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE};
	private Byte XFlags = (byte) 3;
	@SuppressWarnings("unused")
	private boolean enable = true;
	private Punishment configurablePunishment;
	public XRay(Plugin A){
		//p = A;
		A.getServer().getPluginManager().registerEvents(this, A);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent b){
		Player breaker = b.getPlayer();
		Block breakable = b.getBlock();
		if(MiningLoc.get(breaker) == null){
			List<Location> Locs = new ArrayList<Location>();
			Location nullLoc = breakable.getLocation();
			Locs.add(nullLoc);
			MiningLoc.put(breaker, Locs);
		} else {
			List<Location> LocsT = MiningLoc.get(breaker);
			Location nullLoc = breakable.getLocation();
			LocsT.add(nullLoc);
			MiningLoc.put(breaker, LocsT);
		}
		for(Material M : ComparisonMaterial){
			if(breakable.getType() == M){
				Set<Block> S = ScanForMore(breakable);
				Set<Set<Block>> veinset = new HashSet<Set<Block>>();
				veinset.add(S);
				MineralVein.put(breaker, S);
				MineralVeins.put(breaker, veinset);
				if(HeadingFrom(MiningLoc.get(breaker)) && !IsMiningStraight(breaker) && IsMiningInTheDark(breaker)){
					if(Flag.get(breaker) == null){
						Flag.put(breaker, (byte) 1);
					} else {
						Flag.put(breaker, (byte) (Flag.get(breaker)+1));
					}
				}
				if (Flag.get(breaker) != null) {
					if (Flag.get(breaker) > XFlags && !IsMiningStraight(breaker)) {
						Punishment.apply(breaker, configurablePunishment, Reason.XRAY);
						breaker.getInventory().clear();
						Flag.remove(breaker);
						ReplaceDamaged(MineralVeins.get(breaker));
						MineralVein.remove(breaker);
						MineralVeins.remove(breaker);
					} 
				} else {
					Flag.put(breaker, (byte) 0);
				}
			}
		}
	}
	private boolean IsMiningStraight(Player breaker) {
		// TODO Auto-generated method stub
		List<Location> a = MiningLoc.get(breaker);
		List<Location> Y = a.subList(a.size() - 11, a.size()-1);
		for(Location A : Y){
			if(A.getBlockY() == Y.get(0).getBlockY()){
				return true;
			}
		}
		return false;
	}
	private Set<Block> ScanForMore(Block breakable) {
		Set<Block> veinblocks = new HashSet<Block>();
		BlockFace[] blockface = BlockFace.values();
		if(breakable.getType() == ComparisonMaterial[0] ||
		   breakable.getType() == ComparisonMaterial[1] ||
		   breakable.getType() == ComparisonMaterial[2]){
		veinblocks.add(breakable);	
		loc3.add(breakable);
		}
		for(BlockFace bf : blockface){
			if(breakable.getRelative(bf).getType() == ComparisonMaterial[0] ||
				breakable.getRelative(bf).getType() == ComparisonMaterial[1] ||
				breakable.getRelative(bf).getType() == ComparisonMaterial[2]){
				if(!loc3.contains(breakable.getRelative(bf))){
					veinblocks.addAll(ScanForMore(breakable.getRelative(bf)));					
				}
			}
		}
		System.out.println("SIZE OF COLLECTED BLOCKS: " + veinblocks.size());
		return veinblocks;
	}
	@SuppressWarnings("unused")
	public boolean HeadingFrom(List<Location> PlayerLocArray){ //must be touching eachother
			//if a player mines from diamond to diamond he gets flagged
			//if flagged X times(defined in config) he gets baned. Default 3
			//wont flag the player if he is mining straight
		int BX = 0, BY = 0, BZ = 0;
		int PX = 0, PY = 0, PZ = 0;
		int XF = 0;
		List<Byte> deltaX = new ArrayList<Byte>();
		List<Byte> deltaY = new ArrayList<Byte>();
		List<Byte> deltaZ = new ArrayList<Byte>();
		boolean A[] = new boolean[3];
		for(boolean GP : A){
			GP = false;
		}
		for(Location XYZ : PlayerLocArray){
			PX = BX;
			PY = BY;
			PZ = BZ;
			BX = XYZ.getBlockX();
			BY = XYZ.getBlockY();
			BZ = XYZ.getBlockZ();
			XF++;
			if(XF > 0){
				deltaX.add((byte) (BX - PX));
				deltaY.add((byte) (BY - PY));
				deltaZ.add((byte) (BZ - PZ));
			}
		}
			for(int var = 1; var <= deltaX.size() - 1; var++){
				if(deltaX.get(var - 1) == deltaX.get(0)){
					A[0] = true;
				}
			}
			for(int var = 1; var <= deltaY.size() - 1; var++){
				if(deltaY.get(var - 1) > deltaY.get(0) - 2 && deltaY.get(var  -1) < deltaY.get(0) + 2){
					A[1] = true;
				}
			}
			for(int var = 1; var <= deltaY.size() - 1; var++){
				if(deltaZ.get(var - 1) == deltaZ.get(0) - 1){
					A[0] = true;
				}
			}
		return false;
	}
	public void ReplaceDamaged(Set<Set<Block>> mineralVeins2){
		//allows plugin to repop the blocks after banning the XRayer as well as deleting his
		//inventory
		for(Set<Block> A : mineralVeins2){
			for(Block Q : A){
				Q.setType(Material.DIAMOND_ORE);
			}
		}
	}
	public boolean IsMiningInTheDark(Player P){
		byte LL = P.getEyeLocation().getBlock().getLightLevel();
		if(LL < 4){
			return true;
		}
		return false;
	}
	@EventHandler
	public void join(PlayerJoinEvent E){
		
	}
	public void passBoolean(boolean boolean1) {
		// TODO Auto-generated method stub
		setEnable(boolean1);
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public Set<Block> Union(Set<Block> veinblocks, Set<Block> set){
		Set<Block> Q = new HashSet<Block>();
		Q.addAll(veinblocks);
		for(Block H : set){
			if(!veinblocks.contains(H)){
				Q.add(H);
			}
		}
		return Q;
	}
	public void configurePunishment(String pun) throws IllegalArgumentException{
		switch(pun){
			case "BAN":{
				configurablePunishment = Punishment.BAN;
				break;
			}
			case "KICK":{
				configurablePunishment = Punishment.KICK;
				break;
			}
			case "KILL":{
				configurablePunishment = Punishment.KILL;
				break;
			}
			default: throw new IllegalArgumentException("Invalid punishment method - KICK, KILL or BAN are acceptable only");
		}
	}
}
