package lt.standgrounding.exist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryMacro implements Listener {
	private boolean AntiInvMacro = true;
	private HashMap<Player, List<Long>> X = new HashMap<Player, List<Long>>();
	private int VariableStrength = 200;
	private Punishment configurablePunishment;
	public InventoryMacro(Exist plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void MonitorInventory(InventoryClickEvent e){
		if(AntiInvMacro){
			Player A = (Player) e.getWhoClicked();
					List<Long> playerTracker = X.get(A);
					if(playerTracker == null){
						playerTracker = new ArrayList<Long>();
					}
					Long timeNow =  System.currentTimeMillis();
					playerTracker.add(timeNow);
					X.put(A, playerTracker);
					int listLen = playerTracker.size();
					if(listLen > 1){
						long Z = playerTracker.get(listLen-1);
						long B = playerTracker.get(listLen-2);
						if(Module((int) (Z-B)) < VariableStrength){
							Punishment.apply(A, configurablePunishment, Reason.MACRO);
						}
					}
		}
	}
	public void passUnhealthySpeed(int ms){
		this.VariableStrength = ms;
	}
	public void passAntiInv(boolean bool){
		this.AntiInvMacro = bool;
	}
	public int Module(int H){
		if(H < 0){
			return -H;
		} else return H;
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
