package de.Keyle.LoginBuildMe;


import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;



public class LoginBuildMe extends JavaPlugin {
	
	ConfigBuffer cb = new ConfigBuffer(this);
	private LoginBuildMePlayerListener playerListener;
	private LoginBuildMeBlockListener blockListener;
	private LoginBuildMeEntityListener entityListener;
	private Statement statement = null;
	private ResultSet resultSet = null;
	

	@Override
	public void onDisable() 
	{
		cb.log.info("["+ cb.pdfFile.getName() + "] v" + cb.pdfFile.getVersion() + " is disabled!" );
	}

	@Override
	public void onEnable() 
	{
		cb.pdfFile = this.getDescription();
		Configuration config = this.getConfiguration();
		playerListener = new LoginBuildMePlayerListener(cb);
		blockListener = new LoginBuildMeBlockListener(cb);
		entityListener = new LoginBuildMeEntityListener(cb);
		
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Lowest, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		
		config.load();
		
		if((new File(this.getDataFolder().getPath() + File.separator + "config.yml").exists()) == false)
		{
			config.setProperty("Enabled", false);
			config.setProperty("DatabaseServer", "localhost");
			config.setProperty("DatabasePort", "3306");
			config.setProperty("Database", "myDatabase");
			config.setProperty("DatabaseTabelName", "user");
			config.setProperty("DatabaseUser", "root");
			config.setProperty("DatabasePassword", "root");
			config.setProperty("TimeRegister", 60); //In seconds
			config.setProperty("TimeLogin", 60); //In seconds
			config.setProperty("DatabaseFeldName", "username");
			config.setProperty("DatabaseFeldPasswort", "password");
			config.setProperty("DatabaseFeldBuild", "build");
			  
			config.setProperty("Msg_Register", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
			config.setProperty("Msg_kickRegister", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
			config.setProperty("Msg_Login", "Bitte loggen Sie sich ein. Syntax: /login meinpasswort.");
			config.setProperty("Msg_kickLogin", "Sie haben sich innehalb der 60 Sekunden nicht eingeloggt.");
			config.setProperty("Msg_LoginSuccess", "Login erfolgreich!");
			config.setProperty("Msg_kickLoginFail", "Falsches Passwort eingegeben!");
			config.setProperty("Msg_NoLogin", "Falsches Passwort eingegeben!");
			config.setProperty("Msg_BuildPermit", "Sie sind nicht berechtigt Blöcke zu setzten/zerstören");
			
			config.save();
		  
			cb.log.info("You probably startet this plugin the first time. Please edit the configuration file in the your plugins folder!" );
		}
		else
		{
			cb.DatabaseServer = config.getString("DatabaseServer");
			cb.DatabasePort = config.getString("DatabasePort");
			cb.Database = config.getString("Database");
			cb.TableName = config.getString("DatabaseTabelName");
			cb.DatabaseUser = config.getString("DatabaseUser");
			cb.DatabasePassword = config.getString("DatabasePassword");

			cb.DatabaseFeldName = config.getString("DatabaseFeldName", "username");
			cb.DatabaseFeldPasswort = config.getString("DatabaseFeldPasswort", "password");
			cb.DatabaseFeldBuild = config.getString("DatabaseFeldBuild", "build");
			  
			cb.TimeLogin = config.getInt("TimeLogin", 60);
			  
			cb.Msg_Register = config.getString("Msg_Register", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
			cb.Msg_kickRegister = config.getString("Msg_kickRegister", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
			cb.Msg_Login = config.getString("Msg_Login", "Bitte loggen Sie sich ein. Syntax: /login meinpasswort.");
			cb.Msg_kickLogin = config.getString("Msg_kickLogin", "Sie haben sich innehalb der 60 Sekunden nicht eingeloggt.");
			cb.Msg_LoginSuccess = config.getString("Msg_LoginSuccess", "Login erfolgreich!");
			cb.Msg_kickLoginFail = config.getString("Msg_kickLoginFail", "Falsches Passwort eingegeben!");
			cb.Msg_NoLogin = config.getString("Msg_NoLogin", "Falsches Passwort eingegeben!");
			cb.Msg_BuildPermit = config.getString("Msg_BuildPermit", "Sie sind nicht berechtigt Blöcke zu setzten/zerstören");
			
		}
		cb.con = new SQLConnection(cb);
		if(cb.con == null)
		{
			this.onDisable();
		}
		else
		{
			cb.log.info("["+ cb.pdfFile.getName() + "] v" + cb.pdfFile.getVersion() + " is enabled!" );
		}
	}
	
    @Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	String commandName = command.getName().toLowerCase();
        if (sender instanceof Player)
        {
        	if (commandName.equals("login"))
        	{
        		Player player = (Player) sender;
        		if(args.length== 1)
        		{
			        try
					{
						statement = cb.con.connect.createStatement();
						resultSet = statement.executeQuery("SELECT count(*) As Anzahl From "+cb.Database+"."+cb.TableName+" Where "+cb.DatabaseFeldName+" = " + "'" + player.getName()+ "' AND "+cb.DatabaseFeldPasswort+" = md5('"+ args[0] +"')");
						resultSet.next();
						if(resultSet.getInt("Anzahl") == 1)
						{
							player.sendMessage(cb.Msg_LoginSuccess);
							cb.PlayerList.add(player);
							if (CheckBuild(player))
					    	{
					    		cb.PlayerBuildList.add(player);
					    	}
						}else player.kickPlayer(cb.Msg_kickLoginFail);
						statement.close();
						resultSet.close();
						return true;
					}
				    catch(Exception e)
				    {
				    	System.err.println("Exception1: " + e.getMessage());
				    }
        		}
        		else
        		{
        			player.sendMessage(cb.Msg_Login);
        		}
		    }
		    return true;
        }
    	return false;
    }
    
    private boolean CheckBuild(Player player)
	{
		try
		{
			Statement statement = cb.con.connect.createStatement();
			ResultSet resultSet = statement.executeQuery("select "+cb.DatabaseFeldBuild+" From "+cb.Database+"."+cb.TableName+" Where "+cb.DatabaseFeldName+" = " + "'" + player.getName()+ "'" );
    		resultSet.next();
    		if(resultSet.getInt(cb.DatabaseFeldBuild) == 1)
    		{
    			return true;
    		}
    		statement.close();
    		resultSet.close();
    	}
    	catch(Exception e)
   		{
   			System.err.println("Exception2: " + e.getMessage());		   			
   		}
    	return false;
	}
}
