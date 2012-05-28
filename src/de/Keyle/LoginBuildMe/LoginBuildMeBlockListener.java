
package de.Keyle.LoginBuildMe;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class LoginBuildMeBlockListener implements Listener
{

	private ConfigBuffer cb;

	public LoginBuildMeBlockListener(ConfigBuffer cb)
	{
		this.cb = cb;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (!cb.PlayerList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
		if (!cb.PlayerBuildList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_BuildPermit);
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event)
	{
		if (!cb.PlayerList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
		if (!cb.PlayerBuildList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_BuildPermit);
		}
	}
}
