package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

public class WorldChange implements Listener {
    //get plugin
    FlectoneChat plugin = FlectoneChat.getInstance();

    @EventHandler
    public void portalChange(PlayerPortalEvent event){
        //get flectone player
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(event.getPlayer().getName());
        //set color
        flectonePlayer.setWorldColor(event.getTo().getWorld());
    }

    @EventHandler
    public void deathChange(PlayerRespawnEvent event){
        //get flectone player
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(event.getPlayer().getName());
        //set color
        flectonePlayer.setWorldColor(event.getRespawnLocation().getWorld());
    }
}
