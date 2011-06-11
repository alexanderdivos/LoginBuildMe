package de.Keyle.LoginBuildMe;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginBuildMePlayerListener extends PlayerListener{
	
	private ConfigBuffer cb;
	private Location from;
	private Location to;
	private Player player;
	
	    public LoginBuildMePlayerListener(ConfigBuffer cb) {
    		this.cb = cb;
    }
    
   
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
    	if(!cb.PlayerList.contains(event.getPlayer()))
   		{
    		if(!event.getMessage().toLowerCase().startsWith("/login"))
	    	{
	    		event.getPlayer().sendMessage(cb.lv.Msg_Login);
	    		event.setCancelled(true);
	    	}
   		}
    }
    
    @Override
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
		player = event.getPlayer();
    	new LoginTimer(player,cb);
    	cb.PlayerList.remove(event.getPlayer());
    	cb.PlayerBuildList.remove(event.getPlayer());
    }
    
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) 
    {
	    cb.PlayerList.remove(event.getPlayer());
	    cb.PlayerBuildList.remove(event.getPlayer());
    }
    
	@Override
    public void onPlayerMove(PlayerMoveEvent event)
    {
		if(!cb.PlayerList.contains(event.getPlayer()))
		{
	    	from = event.getFrom();
	    	to = event.getTo();
	    	if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ() )
	    	{
	    		event.getPlayer().teleport(from);
	    		event.getPlayer().sendMessage(cb.lv.Msg_Login);
	    	}
		}
    }
    
    @Override
    public void onPlayerChat(PlayerChatEvent event) 
    {
		if(!cb.PlayerList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
    }
    
    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
		if(!cb.PlayerList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(cb.lv.Msg_Login);
		}
    }
	    @Override
    public void onPlayerPickupItem 	(PlayerPickupItemEvent event) 	
    {	    	
		if(!cb.PlayerList.contains(event.getPlayer()))
		{
			event.setCancelled(true);
		}
    }
}