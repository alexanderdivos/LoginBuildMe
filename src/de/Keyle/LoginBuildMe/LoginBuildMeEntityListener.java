package de.Keyle.LoginBuildMe;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class LoginBuildMeEntityListener extends EntityListener{

	private ConfigBuffer cb;
	
	
    public LoginBuildMeEntityListener(ConfigBuffer cb) {
		this.cb = cb;
    }
    	
    @Override
    public void onEntityDamage 	(EntityDamageEvent event)
    { 		
   		Entity entity = event.getEntity();
       if(entity instanceof Player)
       {
    	   	Player player = (Player)entity;
       		if(!cb.PlayerList.contains(player))
       		{
       			event.setCancelled(true);
       		}
       	}
    }
}
