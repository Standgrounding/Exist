package lt.standgrounding.exist;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Punishment {
	KICK, BAN, KILL, FINE;
	public void Punish(Punishment p, Reason r, int Magnitude, Player player) throws IllegalArgumentException{
		//Magnitude is :
		//for bans it marks the duration in days
		//for fine it marks the amount of currency fined in units
		//magnitude of -1 used on ban will permaban the player
		//magnitude of -1 used on fines will set the default currency to 0
		//magnitude has no effect on killing and kicking
		switch(p){
			case BAN:
				long timeNow = System.currentTimeMillis();
				long timeThen = timeNow+1000*60*60*24*Magnitude;
				Date longBan = new Date(timeThen);
				System.out.println("Ban issued for player "+player.getName()+" for "+r.toString());
				String line = "tempban "+player.getName()+" "+longBan+" "+ChatColor.GOLD+r.toString();
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), line);
				break;
			case FINE:
				break;
			case KICK:
				break;
			case KILL:
				break;
			default:
				break;
		}
	}

	static void apply(Player q, Punishment punishment, Reason r) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		String reasonable = r.toString();
		switch(punishment){
		case BAN:
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban "+q.getName()+" "+reasonable);
			break;
		case FINE:
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "");
			break;
		case KICK:
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kick "+q.getName()+" "+reasonable);
			break;
		case KILL:
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill "+q.getName());
			break;
		default:
			throw new IllegalArgumentException("Tried to access a nonexistent punishment method. Contact the developers.");
		}
	}
}
