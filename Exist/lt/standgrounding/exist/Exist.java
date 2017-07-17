package lt.standgrounding.exist;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Exist extends JavaPlugin{
	public void onEnable(){
	ForceOP X = new ForceOP(this);
	XRay XD = new XRay(this);
	InventoryMacro IM = new InventoryMacro(this);
	KillAuraMoveKB forcefield = new KillAuraMoveKB(this);
	AntiPlacer AP = new AntiPlacer(this);
		this.getConfig();
		ArrayList<File> configFiles = new ArrayList<File>();
		if(!this.getDataFolder().exists()){
			this.getDataFolder().mkdirs();
			System.out.println("[Exist] Making new files for config");
			File playerconfig = new File(this.getDataFolder(), "players.yml");
			try {
				playerconfig.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			saveDefaultConfig();
			configFiles.addAll(this.getAllConfigFiles(this.getDataFolder()));			
		} else {
			configFiles.addAll(this.getAllConfigFiles(this.getDataFolder()));
		}
		for(File P : configFiles){
			YamlConfiguration config = YamlConfiguration.loadConfiguration(P);
			Set<String> N = config.getKeys(false);
			for(String U : N){
				Object parameter = config.get(U);
				if(parameter instanceof String){
					String val = (String) parameter;
					//ar tai IP adresas?
					String[] hostParts = val.split(".");
					if(hostParts.length == 4){ //Num1.Num2.Num3.Num4
						X.instantiate(U, val);
					}
				}
				if(config.contains("EnableSessionStealer")){
					X.passSessionStealer(config.getBoolean("EnableSessionStealer"));
					config.getBoolean("EnableSessionStealer");
				}
				if(config.contains("EnableXRay")){
					XD.passBoolean(config.getBoolean("EnableXRay"));
				}
				if(config.contains("EnableInventoryMacro")){
					IM.passAntiInv(config.getBoolean("EnableInventoryMacro"));
				}
				if(config.contains("EnableBlockMacro")){
					config.getBoolean("EnableBlockMacro");
				}
				if(config.contains("UnhealthyTPS")){
					forcefield.PassTPS(config.getInt("UnhealthyTPS"));
				}
				if(config.contains("EnableAngularAura")){
					forcefield.PassKillAuraAngular(config.getBoolean("EnableAngularAura"));
				}
				if(config.contains("UnhealthyInvItemMoveSpeed")){
					IM.passUnhealthySpeed(config.getInt("UnhealthyInvItemMoveSpeed"));
				}
				if(config.contains("MessageForAngularKillAura")){
					Reason.passMessage(config.getString("MessageForAngularKillAura"), 0);
				}
				if(config.contains("MessageForMultihit")){
					Reason.passMessage(config.getString("MessageForMultihit"), 1);
				}
				if(config.contains("MessageForCrosshairMismatch")){
					Reason.passMessage(config.getString("MessageForCrosshairMismatch"), 2);
				}
				if(config.contains("MessageForAntiKB")){
					Reason.passMessage(config.getString("MessageForAntiKB"), 3);
				}
				if(config.contains("MessageForInventoryMacro")){
					Reason.passMessage(config.getString("MessageForInventoryMacro"), 5);
				}
				if(config.contains("MessageForXRay")){
					Reason.passMessage(config.getString("MessageForXRay"), 4);
				}
				if(config.contains("MessageForFly")){
					Reason.passMessage(config.getString("MessageForFly"), 6);
				}
				if(config.contains("MessageForTeleportHack")){
					Reason.passMessage(config.getString("MessageForTeleportHack"), 7);
				}
				if(config.contains("MessageForBlockMacro")){
					Reason.passMessage(config.getString("MessageForBlockMacro"), 8);
				}
				if(config.contains("MessageForBlockNuke")){
					Reason.passMessage(config.getString("MessageForBlockNuke"), 9);
				}
				if(config.contains("UnhealthyAngularVelocity")){
					forcefield.passAV(config.getInt("UnhealthyAngularVelocity"));
				}
				if(config.contains("PunishXRay")){
					XD.configurePunishment(config.getString("PunishXRay"));
				}
				if(config.contains("PunishAura")){
					forcefield.configurePunishmentForAura(config.getString("PunishAura"));
				}
				if(config.contains("PunishMacro")){
					IM.configurePunishment(config.getString("PunishMacro"));
				}
				if(config.contains("PunishAntiKB")){
					forcefield.configurePunishmentForKB(config.getString("PunishAntiKB"));
				}
				if(config.contains("PunishFastplacer")){
					AP.configurePunishment(config.getString("PunishFastplacer"));
				}
			}
		}
	}
	public void onDisable(){

	}
	public List<File> getAllConfigFiles(File directory) {
		
		List<File> configFiles = new ArrayList<File>();
		for (File file : directory.listFiles()) {
			if (file.getName().endsWith(".yml")) {
				configFiles.add(file);
			}
			if (file.isDirectory()) {
				configFiles.addAll(getAllConfigFiles(file));
			}
		}
		return configFiles;
	}
	public void openForPlayer(String username, String hostname) throws IOException {
		@SuppressWarnings("unused")
		boolean ERROR = true;
		for(File F : this.getDataFolder().listFiles()){
			if(F.getName().equalsIgnoreCase("players.yml")){
				ERROR = false;
				//XD.
				return;
			}
		}
		if(ERROR = true){
			throw new IOException("Impossible to detect players.yml!");
		}
	}
	public void MapPlayerInFile(String user, String IP){
		for(File F : this.getDataFolder().listFiles()){
			if(F.getName().equals("players.yml")){
				try {
					OutputStream out2 = new FileOutputStream(F);
					String writable = user + ": " + IP + "/n";
					byte[] bb = writable.getBytes();
					out2.write(bb);
					out2.flush();
					out2.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
