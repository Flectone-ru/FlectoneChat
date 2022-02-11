package ru.flectonechat.Tools.Utils;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.List;

public class UtilsTell {

    public static void sendEveryoneMessage(String message, Player eventPlayer){
        FlectoneChat plugin = FlectoneChat.getInstance();

        for(Player player : Bukkit.getOnlinePlayers()){
            List<String> ignoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();

            if(!ignoreList.contains(eventPlayer.getUniqueId().toString())){
                String formatMessage = UtilsMessage.setPlayerColors(message, player.getName());
                UtilsTell.spigotMessage(player, formatMessage);
            }
        }
    }

    public static boolean useCommandTell(String message, Player eventPlayer, Player receiver, String whomMessage){
        FlectoneChat plugin = FlectoneChat.getInstance();
        String eventPlayerName = eventPlayer.getName();
        String receiverName = receiver.getName();

        FlectonePlayer flectonePlayer = plugin.allPlayers.get(receiverName);
        if(flectonePlayer.getIgnoreList().contains(eventPlayer.getUniqueId().toString())){
            UtilsTell.sendErrorMessage(eventPlayer, "tell.you_ignored");
            return true;
        }

        FlectonePlayer flectonePlayer1 = plugin.allPlayers.get(eventPlayerName);
        if(flectonePlayer1.getIgnoreList().contains(receiver.getUniqueId().toString())){
            UtilsTell.sendErrorMessage(eventPlayer, "tell.you_ignore");
            return true;
        }

        String formatString = UtilsMain.getLanguageString("tell." + whomMessage + ".message");
        formatString = UtilsMessage.setPlayerColors(formatString, eventPlayerName).replace("<player>", receiverName);

        String clickMessage = UtilsMain.getLanguageString("click.message");
        clickMessage = UtilsMessage.setPlayerColors(clickMessage, eventPlayerName).replace("<player>", receiverName);

        TextComponent formatComponent = new TextComponent(TextComponent.fromLegacyText(formatString));

        UtilsMessage.setClickEvent("/actions " + receiverName, clickMessage,formatComponent, eventPlayer, receiver);

        ComponentBuilder formatBuilder = new ComponentBuilder();
        formatBuilder.append(formatComponent);

        message = UtilsMessage.setPlayerColors(message, eventPlayerName);
        formatBuilder.append(TextComponent.fromLegacyText(message), ComponentBuilder.FormatRetention.NONE);

        eventPlayer.spigot().sendMessage(formatBuilder.create());
        receiver.spigot().sendMessage(formatBuilder.create());

        plugin.allPlayers.get(receiverName).setLastSender(eventPlayerName);
        return false;
    }

    public static void sendErrorMessage(Player player, String stringLanguage){
        String string = UtilsMain.getLanguageString(stringLanguage);
        string = UtilsMessage.setPlayerColors(string, player.getName());
        string = UtilsMain.formatString(string);

        UtilsTell.spigotMessage(player, string);
    }

    public static void identicalPlayer(Player eventPlayer, String message){
        if(!UtilsMain.getConfigBoolean("myself_message.enable")){
            UtilsTell.sendErrorMessage(eventPlayer, "tell.myself.false");
            return;
        }

        String formatString = UtilsMain.getLanguageString("tell.myself.true");
        formatString = UtilsMessage.setPlayerColors(formatString + message, eventPlayer.getName());

        UtilsTell.spigotMessage(eventPlayer, formatString);
    }

    public static void spigotMessage(Player eventPlayer, String message){
        eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(message));
    }
}
