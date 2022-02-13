package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import ru.flectonechat.Tools.Utils.UtilsMain;

public class WorldChange implements Listener {

    @EventHandler
    public void portalChange(PlayerPortalEvent event){
        //set color
        UtilsMain.useSetWorldColor(event.getPlayer(), event.getTo().getWorld());
    }

    @EventHandler
    public void deathChange(PlayerRespawnEvent event){
        //set color
        UtilsMain.useSetWorldColor(event.getPlayer(), event.getRespawnLocation().getWorld());
    }
}
