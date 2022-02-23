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

        Player eventPlayer = event.getPlayer();
        //create flectone player for player
        UtilsMain.createFlectonePlayer(eventPlayer);
        //set tab list header
        checkTabList(eventPlayer, "header");
        checkTabList(eventPlayer, "footer");

        if(UtilsMain.getConfigBoolean("join.message.enable")){
            //message for console
            plugin.getLogger().info(event.getJoinMessage());
            event.setJoinMessage("");

            sendMessage("join.message", eventPlayer.getName());
        }

        if(UtilsMain.getConfigBoolean("join.greeting.message.enable")){

            String message = UtilsMessage.defaultLanguageString("join.greeting.message", eventPlayer.getName());

            UtilsTell.sendMessage(eventPlayer, message);
        }
    }

    @EventHandler
    public void PlayerLeft(PlayerQuitEvent event){

        Player eventPlayer = event.getPlayer();

        if(UtilsMain.getConfigBoolean("left.message.enable")){
            //message for console
            plugin.getLogger().info(event.getQuitMessage());
            event.setQuitMessage("");

            sendMessage("left.message", eventPlayer.getName());
        }

        plugin.allPlayers.remove(eventPlayer.getName());
    }
    //check and set tab list
    public static void checkTabList(Player player, String tabListName){
        String config = "tab-list." + tabListName;

        if(UtilsMain.getConfigBoolean(config + ".enable")){

            String tabList = UtilsMain.getLanguageString(config);
            tabList = UtilsMessage.setPlayerColors(tabList, player.getName());
            //set header and footer
            if(tabListName.equals("header")) player.setPlayerListHeader(tabList) ;
            if(tabListName.equals("footer")) player.setPlayerListFooter(tabList);
        }
    }
    //send message
    private static void sendMessage(String stringName, String eventPlayerName){
        for(Player player : Bukkit.getOnlinePlayers()){
            //set color for this player
            String message = UtilsMain.getLanguageString(stringName);
            message = UtilsMessage.replacePlayerName(message, eventPlayerName);
            message = UtilsMessage.setPlayerColors(message, player.getName());

            UtilsTell.sendMessage(player, message);
        }
    }


}
