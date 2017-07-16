package lt.standgrounding.exist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiPlacer implements Listener{
	private HashMap<Player, List<Long>> timemap = new HashMap<Player, List<Long>>();
	private int UnhealthyBS = 20;
	private Punishment configurablePunishment;
	AntiPlacer(Exist plug){
		plug.getServer().getPluginManager().registerEvents(this, plug);
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent E){
		Player builder = E.getPlayer();
		Long timeNow = System.currentTimeMillis();
		List<Long> playertimes = timemap.get(builder);
		if(playertimes == null){
			playertimes = new ArrayList<Long>();
		}
		playertimes.add(timeNow);
		timemap.put(builder, playertimes);
		//--------------------------------------------------------------------------
		if(playertimes != null && playertimes.size() >= 2){
			long dT = playertimes.get(0) - playertimes.get(1);
			if(Module((int) dT) < UnhealthyBS){
				Punishment.apply(builder, configurablePunishment, Reason.FASTPLACER);
			}
		}
	}
	public void passUnhealthyBS(int A){
		UnhealthyBS = A;
	}
	public int Module(int paramInt){
		if(paramInt < 0) return -paramInt;
		else return paramInt;
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
