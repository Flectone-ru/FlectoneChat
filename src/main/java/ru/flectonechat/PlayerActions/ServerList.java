package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;

public class ServerList implements Listener {

    @EventHandler
    public void serverList(ServerListPingEvent event){
        //set custom motd
        setServerMotd(event);
        //set custom online
        setOnlineNumber(event);
    }
    //set custom motd
    private static void setServerMotd(ServerListPingEvent event){
        //get boolean
        if(UtilsMain.getConfigBoolean("server.motd.enable")){
            //get message
            String motd = UtilsMain.getLanguageString("server-motd-message");
            motd = UtilsMessage.formatString(motd);
            //set motd
            event.setMotd(motd);
        }
    }
    //set custom online
    private static void setOnlineNumber(ServerListPingEvent event){
        //get boolean
        if(UtilsMain.getConfigBoolean("server.online.enable")){
            //get int
            int number = UtilsMain.getConfigInt("server.online.number");
            //set int
            event.setMaxPlayers(number);
        }
    }
}
