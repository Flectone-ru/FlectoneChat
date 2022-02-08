package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

public class WorldChange implements Listener {

    FlectoneChat plugin = FlectoneChat.getInstance();

    @EventHandler
    public void portalChange(PlayerPortalEvent event){
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(event.getPlayer().getName());

        flectonePlayer.setWorldColor(event.getTo().getWorld());
    }

    @EventHandler
    public void deathChange(PlayerRespawnEvent event){
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(event.getPlayer().getName());

        flectonePlayer.setWorldColor(event.getRespawnLocation().getWorld());
    }
}
