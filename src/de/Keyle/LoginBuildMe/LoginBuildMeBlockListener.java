package de.Keyle.LoginBuildMe;

import org.bukkit.event.block.BlockDamageEvent;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LoginBuildMeBlockListener extends BlockListener {

	private ConfigBuffer cb;
	
    public LoginBuildMeBlockListener(ConfigBuffer cb) {
		this.cb = cb;
    }
	
    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
		if(cb.PlayerList.contains(event.getPlayer()) == false)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
		if (cb.PlayerBuildList.contains(event.getPlayer()) == false)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_BuildPermit);
		}
    }
    
    @Override
    public void onBlockDamage(BlockDamageEvent event)
    {
		if(cb.PlayerList.contains(event.getPlayer()) == false)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
		if (cb.PlayerBuildList.contains(event.getPlayer()) == false)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_BuildPermit);
		}
    }
}
