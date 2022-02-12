package ru.flectonechat.PlayerActions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;


public class JoinAndLeft implements Listener {

    FlectoneChat plugin = FlectoneChat.getInstance();

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        //event player
        Player eventPlayer = event.getPlayer();
        //create flectone player for player
        UtilsMain.createFlectonePlayer(eventPlayer);
        //set tab list header
        checkTabListHeader(eventPlayer);
        //set join message
        if(UtilsMain.getConfigBoolean("join.message.enable")){
            //message for console
            plugin.getLogger().info(event.getJoinMessage());
            event.setJoinMessage("");
            //send message
            sendMessage("join.message", eventPlayer.getName());
        }
        //set greeting message
        if(UtilsMain.getConfigBoolean("join.greeting.message.enable")){
            //get greeting message
            String message = UtilsMessage.defaultLanguageString("join.greeting.message", eventPlayer.getName());
            //send message
            UtilsTell.sendMessage(eventPlayer, message);
        }
    }

    @EventHandler
    public void PlayerLeft(PlayerQuitEvent event){
        //event player
        Player eventPlayer = event.getPlayer();
        //set left message
        if(UtilsMain.getConfigBoolean("left.message.enable")){
            //message for console
            plugin.getLogger().info(event.getQuitMessage());
            event.setQuitMessage("");
            //send message
            sendMessage("left.message", eventPlayer.getName());
        }
        //remove flectone player for player
        plugin.allPlayers.remove(eventPlayer.getName());
    }
    //check and set tab list header
    private static void checkTabListHeader(Player player){
        //get boolean
        if(UtilsMain.getConfigBoolean("tab-list.header" + ".enable")){
            //get string
            String tabListHeader = UtilsMain.getLanguageString("tab-list.header");
            tabListHeader = UtilsMessage.setPlayerColors(tabListHeader, player.getName());
            //set header
            player.setPlayerListHeader(tabListHeader);
        }
    }
    //send message
    private static void sendMessage(String stringName, String eventPlayerName){
        for(Player player : Bukkit.getOnlinePlayers()){
            //set color for this player
            String message = UtilsMain.getLanguageString(stringName);
            message = UtilsMessage.replacePlayerName(message, eventPlayerName);
            message = UtilsMessage.setPlayerColors(message, player.getName());
            //send message
            UtilsTell.sendMessage(player, message);
        }
    }


}
