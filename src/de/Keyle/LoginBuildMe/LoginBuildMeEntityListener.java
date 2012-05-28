
package de.Keyle.LoginBuildMe;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class LoginBuildMeEntityListener implements Listener
{

	private ConfigBuffer cb;

	public LoginBuildMeEntityListener(ConfigBuffer cb)
	{
		this.cb = cb;
	}

    @EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof Player)
		{
			Player player = (Player) entity;
			if (!cb.PlayerList.contains(player))
			{
				event.setCancelled(true);
			}
		}
	}
}
