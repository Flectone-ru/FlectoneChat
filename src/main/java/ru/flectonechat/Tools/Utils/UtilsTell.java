package ru.flectonechat.Tools.Utils;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.List;

public class UtilsTell {
    //send message to everyone
    public static void sendEveryoneMessage(String message, Player eventPlayer){

        FlectoneChat plugin = FlectoneChat.getInstance();

        for(Player player : Bukkit.getOnlinePlayers()){

            List<String> ignoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();

            if(!ignoreList.contains(eventPlayer.getUniqueId().toString())){

                String formatMessage = UtilsMessage.setPlayerColors(message, player.getName());
                UtilsTell.sendMessage(player, formatMessage);
            }
        }
    }
    //use command tell
    public static boolean useCommandTell(String message, Player eventPlayer, Player receiver, String whomMessage){

        FlectoneChat plugin = FlectoneChat.getInstance();

        String eventPlayerName = eventPlayer.getName();
        String receiverName = receiver.getName();

        FlectonePlayer flectonePlayerReceiver = plugin.allPlayers.get(receiverName);
        //if receiver ignore event player
        if(flectonePlayerReceiver.getIgnoreList().contains(eventPlayer.getUniqueId().toString())){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.you-ignored");
            return true;
        }

        FlectonePlayer flectonePlayerSender = plugin.allPlayers.get(eventPlayerName);
        //if event player ignore receiver
        if(flectonePlayerSender.getIgnoreList().contains(receiver.getUniqueId().toString())){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.you-ignore");
            return true;
        }

        String formatString = UtilsMain.getLanguageString("tell." + whomMessage + ".message");
        formatString = UtilsMessage.setPlayerColors(formatString, eventPlayerName);
        formatString = UtilsMessage.replacePlayerName(formatString, receiverName);

        formatString = UtilsMessage.replaceVaultPlaceholders(formatString, eventPlayer);

        String clickMessage = UtilsMain.getLanguageString("click.message");
        clickMessage = UtilsMessage.setPlayerColors(clickMessage, eventPlayerName);
        clickMessage = UtilsMessage.replacePlayerName(clickMessage, receiverName);

        TextComponent formatComponent = new TextComponent(TextComponent.fromLegacyText(formatString));

        UtilsMessage.setClickEvent("/actions " + receiverName, clickMessage,formatComponent, eventPlayer, receiver);

        ComponentBuilder formatBuilder = new ComponentBuilder();
        formatBuilder.append(formatComponent);

        message = UtilsMessage.setPlayerColors(message, eventPlayerName);

        formatBuilder.append(TextComponent.fromLegacyText(message), ComponentBuilder.FormatRetention.NONE);

        eventPlayer.spigot().sendMessage(formatBuilder.create());
        //save last sender
        plugin.allPlayers.get(receiverName).setLastSender(eventPlayerName);
        return false;
    }
    //get message from language file and send
    public static void sendMessageLanguage(Player player, String stringLanguage){

        String string = UtilsMain.getLanguageString(stringLanguage);
        string = UtilsMessage.setPlayerColors(string, player.getName());

        UtilsTell.sendMessage(player, string);
    }
    //check and send message myself
    public static void identicalPlayer(Player eventPlayer, String message){

        if(!UtilsMain.getConfigBoolean("myself-message.enable")){

            UtilsTell.sendMessageLanguage(eventPlayer, "tell.myself.false");
            return;
        }

        String formatString = UtilsMain.getLanguageString("tell.myself.true");
        formatString = formatString + message;
        formatString = UtilsMessage.setPlayerColors(formatString, eventPlayer.getName());

        UtilsTell.sendMessage(eventPlayer, formatString);
    }
    //simple send message
    public static void sendMessage(Player eventPlayer, String message){
        eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(message));
    }
}
