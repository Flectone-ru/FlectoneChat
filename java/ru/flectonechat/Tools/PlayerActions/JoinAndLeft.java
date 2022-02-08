package ru.flectonechat.PlayerActions;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utilities;


public class JoinAndLeft implements Listener {

    FlectoneChat plugin = FlectoneChat.getInstance();

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){

        Player eventPlayer = event.getPlayer();

        Utilities.createFlectonePlayer(eventPlayer);

        checkTabListHeader(eventPlayer, "tab_list.header");

        if(Utilities.getConfigBoolean("join.message.enable")){
            plugin.getLogger().info(event.getJoinMessage());
            event.setJoinMessage("");


            for(Player player : Bukkit.getOnlinePlayers()){
                String message = Utilities.getLanguageString("join.message");
                message = Utilities.replaceMessagePlayer(message, eventPlayer.getName());
                message = Utilities.setPlayerColors(message, player.getName());
                player.spigot().sendMessage(TextComponent.fromLegacyText(message));
            }
        }

        if(Utilities.getConfigBoolean("join.greeting.message.enable")){
            String message = Utilities.getLanguageString("join.greeting.message");
            message = Utilities.setPlayerColors(message, eventPlayer.getName());
            message = Utilities.replaceMessagePlayer(message, eventPlayer.getName());

            eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(message));
        }

    }

    @EventHandler
    public void PlayerLeft(PlayerQuitEvent event){
        Player eventPlayer = event.getPlayer();

        if(Utilities.getConfigBoolean("left.message.enable")){
            plugin.getLogger().info(event.getQuitMessage());
            event.setQuitMessage("");

            for(Player player : Bukkit.getOnlinePlayers()){
                String message = Utilities.getLanguageString("left.message");
                message = Utilities.replaceMessagePlayer(message, eventPlayer.getName());
                message = Utilities.setPlayerColors(message, player.getName());
                player.spigot().sendMessage(TextComponent.fromLegacyText(message));
            }
        }

        plugin.allPlayers.remove(eventPlayer.getName());
    }

    private static void checkTabListHeader(Player player, String booleanName){
        if(Utilities.getConfigBoolean(booleanName + ".enable")){
            String tabListHeader = Utilities.getLanguageString(booleanName);
            tabListHeader = Utilities.setPlayerColors(tabListHeader, player.getName());

            player.setPlayerListHeader(tabListHeader);
        }
    }


}
