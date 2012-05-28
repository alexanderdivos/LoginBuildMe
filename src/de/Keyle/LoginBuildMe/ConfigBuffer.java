
package de.Keyle.LoginBuildMe;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConfigBuffer
{

	public Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdfFile;
	public LoginBuildMe Plugin;

	public DatabaseVariables dv = new DatabaseVariables();
	public LanguageVariables lv = new LanguageVariables();

	public int TimeLogin;
	public SQLConnection con;

	public List<Player> PlayerList = new ArrayList<Player>();
	public List<Player> PlayerBuildList = new ArrayList<Player>();

	public ConfigBuffer(LoginBuildMe Plugin)
	{
		this.Plugin = Plugin;
	}

}

class DatabaseVariables extends Property
{
	public String DatabaseServer;
	public String DatabasePort;
	public String Database;
	public String TableName;
	public String DatabaseUser;
	public String DatabasePassword;
	public String DatabaseFeldName;
	public String DatabaseFeldPasswort;
	public String DatabaseFeldBuild;
}

class LanguageVariables extends Property
{
	public String Msg_Register;
	public String Msg_kickRegister;
	public String Msg_Login;
	public String Msg_kickLogin;
	public String Msg_LoginSuccess;
	public String Msg_kickLoginFail;
	public String Msg_NoLogin;
	public String Msg_BuildPermit;
}

class Property
{
	public void setProperty(FileConfiguration cfg, String key, Object value)
	{
		if (cfg.get(key) == null)
		{
			cfg.set(key, value);
		}
	}
}
