package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;

public class ServerList implements Listener {

    @EventHandler
    public void serverList(ServerListPingEvent event){
        setServerMotd(event);
        setOnlineNumber(event);
    }
    //set custom motd
    private static void setServerMotd(ServerListPingEvent event){

        if(UtilsMain.getConfigBoolean("server.motd.enable")){

            String motd = UtilsMain.getLanguageString("server-motd-message");
            motd = UtilsMessage.formatString(motd);

            event.setMotd(motd);
        }
    }
    //set custom online
    private static void setOnlineNumber(ServerListPingEvent event){

        if(UtilsMain.getConfigBoolean("server.online.enable")){
            int number = UtilsMain.getConfigInt("server.online.number");
            event.setMaxPlayers(number);
        }
    }
}
