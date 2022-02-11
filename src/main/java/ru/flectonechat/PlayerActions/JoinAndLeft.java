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

        UtilsMain.createFlectonePlayer(eventPlayer);

        checkTabListHeader(eventPlayer, "tab_list.header");

        if(UtilsMain.getConfigBoolean("join.message.enable")){
            plugin.getLogger().info(event.getJoinMessage());
            event.setJoinMessage("");

            setMessage("join.message", eventPlayer.getName());
        }

        if(UtilsMain.getConfigBoolean("join.greeting.message.enable")){
            String message = UtilsMessage.defaultLanguageString("join.greeting.message", eventPlayer.getName());
            UtilsTell.spigotMessage(eventPlayer, message);
        }

    }

    @EventHandler
    public void PlayerLeft(PlayerQuitEvent event){
        Player eventPlayer = event.getPlayer();

        if(UtilsMain.getConfigBoolean("left.message.enable")){
            plugin.getLogger().info(event.getQuitMessage());
            event.setQuitMessage("");

            setMessage("left.message", eventPlayer.getName());
        }

        plugin.allPlayers.remove(eventPlayer.getName());
    }

    private static void checkTabListHeader(Player player, String booleanName){
        if(UtilsMain.getConfigBoolean(booleanName + ".enable")){
            String tabListHeader = UtilsMain.getLanguageString(booleanName);
            tabListHeader = UtilsMessage.setPlayerColors(tabListHeader, player.getName());

            player.setPlayerListHeader(tabListHeader);
        }
    }

    private static void setMessage(String stringName, String eventPlayerName){
        for(Player player : Bukkit.getOnlinePlayers()){
            String message = UtilsMain.getLanguageString(stringName);
            message = UtilsMessage.replacePlayerName(message, eventPlayerName);
            message = UtilsMessage.setPlayerColors(message, player.getName());
            UtilsTell.spigotMessage(player, message);
        }
    }


}
