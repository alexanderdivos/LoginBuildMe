package de.Keyle.LoginBuildMe;

import org.bukkit.entity.Player;

public class LoginTimer {

	private int counter = 0;
	public Player player;
	private int RespawnTimer;

	public LoginTimer(final Player player, final ConfigBuffer cb)
	{
		this.player = player;
		player.sendMessage(cb.lv.Msg_Login);
		RespawnTimer = cb.Plugin.getServer().getScheduler().scheduleSyncRepeatingTask(cb.Plugin,new Runnable() 
		{
			public void run() {
				if (counter >= cb.TimeLogin)
				{
					player.kickPlayer(cb.lv.Msg_kickLogin);
					cb.Plugin.getServer().getScheduler().cancelTask(RespawnTimer);
				}
				else if(player.isOnline() == false || cb.PlayerList.contains(player))
				{
					cb.Plugin.getServer().getScheduler().cancelTask(RespawnTimer);
				}
				else counter++;
			}
		}, 0L,20L);
	}
	
}