package lt.standgrounding.exist;

import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryMacro implements Listener {
	private boolean AntiInvMacro = true;
	private HashMap<Player, List<Short>> X = new HashMap<Player, List<Short>>();
	private short VariableStrength = 100;
	public InventoryMacro(Exist plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void MonitorInventory(InventoryMoveItemEvent e){
		if(AntiInvMacro){
			InventoryHolder H = e.getSource().getHolder();
			InventoryHolder Q = e.getDestination().getHolder();
			if(H instanceof Chest || H instanceof DoubleChest || H instanceof ShulkerBox){
				if(Q instanceof Player){
					List<Short> playerTracker = X.get((Player) Q);
					Short timeNow = (Short) (short) System.currentTimeMillis();
					playerTracker.add(timeNow);
					X.put((Player) Q, playerTracker);
					short listLen = (short) playerTracker.size();
					if(listLen > 1){
						int A = playerTracker.get(listLen-1);
						int B = playerTracker.get(listLen-2);
						if(A-B < VariableStrength){
							Punishment.apply((Player) Q, Punishment.BAN, Reason.MACRO);
						}
					}
				}
			}
		}
	}
	public void passUnhealthySpeed(int ms){
		this.VariableStrength = (short) ms;
	}
	public void passAntiInv(boolean bool){
		this.AntiInvMacro = bool;
	}
}
