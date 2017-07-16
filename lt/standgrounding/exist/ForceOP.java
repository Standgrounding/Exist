package lt.standgrounding.exist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class ForceOP implements Listener {
	@SuppressWarnings("unused")
	private Plugin p;
	protected Set<Player> flagged = new HashSet<Player>();
	protected Map<String, String> Q = new HashMap<String,String>();
	private boolean SessionStealer = true;
	ForceOP(Exist plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		p = plugin;
	}
	//deForceOP - book hack, ...
	@EventHandler
	public void onCmd(ServerCommandEvent e){
		CommandSender S = e.getSender();
		String Q = e.getCommand();
		String[] cont = {"op", "fill", "replace", "kill", "pos1", "pos2", "ban", "kick", "gamemode", "authme"};
		if(containsAnyOfTheFollowing(Q, cont)){
			if(S instanceof BlockCommandSender || S instanceof ArmorStand || S instanceof Item){
				e.setCancelled(true);	
			}
			if(S instanceof Player){
				Player SendPlayer = (Player) e.getSender();
				if(flagged.contains(SendPlayer)){
				e.setCancelled(true);
				SendPlayer.sendMessage(ChatColor.GOLD+"[Exist]"+ChatColor.GREEN+"[SessionStealer]"+ChatColor.YELLOW+"Jums"+
				" neleista daryti ðiø veiksmø naudojantis komandomis");
				}
			}
		}
	}
	//"session stealer"
	@EventHandler
	public void onLog(PlayerLoginEvent E){
		if (SessionStealer ) {
			String name = E.getPlayer().getName();
			String host = E.getHostname();
			boolean B = compareSessionIPs(name, host);
			if (!B) {
				flag(E.getPlayer());
			} 
		}
	}
	private void flag(Player player) {
		// TODO Auto-generated method stub
		flagged.add(player);
	}
	private boolean compareSessionIPs(String name, String host) {
		// true = OK false = blogai
		if(Q.containsKey(name)){
			String R = Q.get(name);
			if(R == host){
				return true;
			}
			return false;
		} else {
			Q.put(name, host);
			return true;
		}
	}
	protected Map<String, String> FMap(){
		return Q;
	}
	protected void instantiate(String A, String B){
		Q.put(A, B);
	}
	protected boolean containsAnyOfTheFollowing(String A, String[] Y){
		for(String N : Y){
			if(A.contains(N)) return true;
		}
		return false;
	}
	protected void passSessionStealer(boolean SS){
		this.SessionStealer = SS;
	}
}
