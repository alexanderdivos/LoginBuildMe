
package de.Keyle.LoginBuildMe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.Statement;

public class LoginBuildMe extends JavaPlugin
{

	ConfigBuffer cb = new ConfigBuffer(this);

    public void onDisable()
	{
		cb.log.info("[" + cb.pdfFile.getName() + "] v" + cb.pdfFile.getVersion() + " is disabled!");
	}

	public void onEnable()
	{
		cb.pdfFile = this.getDescription();
		FileConfiguration config = this.getConfig();
        LoginBuildMePlayerListener playerListener = new LoginBuildMePlayerListener(cb);
        LoginBuildMeBlockListener blockListener = new LoginBuildMeBlockListener(cb);
        LoginBuildMeEntityListener entityListener = new LoginBuildMeEntityListener(cb);

		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(entityListener, this);

		cb.dv.setProperty(config, "DatabaseServer", "localhost");
		cb.dv.setProperty(config, "DatabasePort", "3306");
		cb.dv.setProperty(config, "Database", "myDatabase");
		cb.dv.setProperty(config, "DatabaseTabelName", "user");
		cb.dv.setProperty(config, "DatabaseUser", "root");
		cb.dv.setProperty(config, "DatabasePassword", "root");
		cb.dv.setProperty(config, "TimeLogin", 60); //In seconds
		cb.dv.setProperty(config, "DatabaseFeldName", "username");
		cb.dv.setProperty(config, "DatabaseFeldPasswort", "password");
		cb.dv.setProperty(config, "DatabaseFeldBuild", "build");

		cb.lv.setProperty(config, "Msg_Register", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
		cb.lv.setProperty(config, "Msg_kickRegister", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
		cb.lv.setProperty(config, "Msg_Login", "Bitte loggen Sie sich ein. Syntax: /login meinpasswort.");
		cb.lv.setProperty(config, "Msg_kickLogin", "Sie haben sich innehalb der 60 Sekunden nicht eingeloggt.");
		cb.lv.setProperty(config, "Msg_LoginSuccess", "Login erfolgreich!");
		cb.lv.setProperty(config, "Msg_kickLoginFail", "Falsches Passwort eingegeben!");
		cb.lv.setProperty(config, "Msg_NoLogin", "Falsches Passwort eingegeben!");
		cb.lv.setProperty(config, "Msg_BuildPermit", "Sie sind nicht berechtigt Bl�cke zu setzten/zerst�ren");

        this.saveConfig();

		cb.dv.DatabaseServer = config.getString("DatabaseServer");
		cb.dv.DatabasePort = config.getString("DatabasePort");
		cb.dv.Database = config.getString("Database");
		cb.dv.TableName = config.getString("DatabaseTabelName");
		cb.dv.DatabaseUser = config.getString("DatabaseUser");
		cb.dv.DatabasePassword = config.getString("DatabasePassword");

		cb.dv.DatabaseFeldName = config.getString("DatabaseFeldName", "username");
		cb.dv.DatabaseFeldPasswort = config.getString("DatabaseFeldPasswort", "password");
		cb.dv.DatabaseFeldBuild = config.getString("DatabaseFeldBuild", "build");

		cb.TimeLogin = config.getInt("TimeLogin", 60);

		cb.lv.Msg_Register = config.getString("Msg_Register", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
		cb.lv.Msg_kickRegister = config.getString("Msg_kickRegister", "Bitte registrieren Sie sich auf www.mc.sfe-hosting.de");
		cb.lv.Msg_Login = config.getString("Msg_Login", "Bitte loggen Sie sich ein. Syntax: /login meinpasswort.");
		cb.lv.Msg_kickLogin = config.getString("Msg_kickLogin", "Sie haben sich innehalb der 60 Sekunden nicht eingeloggt.");
		cb.lv.Msg_LoginSuccess = config.getString("Msg_LoginSuccess", "Login erfolgreich!");
		cb.lv.Msg_kickLoginFail = config.getString("Msg_kickLoginFail", "Falsches Passwort eingegeben!");
		cb.lv.Msg_NoLogin = config.getString("Msg_NoLogin", "Falsches Passwort eingegeben!");
		cb.lv.Msg_BuildPermit = config.getString("Msg_BuildPermit", "Sie sind nicht berechtigt Bl�cke zu setzten/zerst�ren");

		cb.con = new SQLConnection(cb);
		if (cb.con == null)
		{
			this.onDisable();
		}
		else
		{
			cb.log.info("[" + cb.pdfFile.getName() + "] v" + cb.pdfFile.getVersion() + " is enabled!");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		String commandName = command.getName().toLowerCase();
		if (sender instanceof Player)
		{
			if (commandName.equals("login"))
			{
				Player player = (Player) sender;
				if (args.length == 1)
				{
					try
					{
                        Statement statement = cb.con.connect.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT count(*) As Anzahl From " + cb.dv.Database + "." + cb.dv.TableName + " Where " + cb.dv.DatabaseFeldName + " = " + "'" + player.getName() + "' AND " + cb.dv.DatabaseFeldPasswort + " = md5('" + args[0] + "')");
						resultSet.next();
						if (resultSet.getInt("Anzahl") == 1)
						{
							player.sendMessage(cb.lv.Msg_LoginSuccess);
							cb.PlayerList.add(player);
							if (CheckBuild(player))
							{
								cb.PlayerBuildList.add(player);
							}
						}
						else
							player.kickPlayer(cb.lv.Msg_kickLoginFail);
						statement.close();
						resultSet.close();
						return true;
					}
					catch (Exception e)
					{
						System.err.println("Exception1: " + e.getMessage());
					}
				}
				else
				{
					player.sendMessage(cb.lv.Msg_Login);
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
			ResultSet resultSet = statement.executeQuery("select " + cb.dv.DatabaseFeldBuild + " From " + cb.dv.Database + "." + cb.dv.TableName + " Where " + cb.dv.DatabaseFeldName + " = " + "'" + player.getName() + "'");
			resultSet.next();
			if (resultSet.getInt(cb.dv.DatabaseFeldBuild) == 1)
			{
				return true;
			}
			statement.close();
			resultSet.close();
		}
		catch (Exception e)
		{
			System.err.println("Exception2: " + e.getMessage());
		}
		return false;
	}
}
