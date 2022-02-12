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
        //get plugin
        FlectoneChat plugin = FlectoneChat.getInstance();
        //get online players
        for(Player player : Bukkit.getOnlinePlayers()){
            //get ignore list player
            List<String> ignoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();
            //if player doesn't ignore event player
            if(!ignoreList.contains(eventPlayer.getUniqueId().toString())){
                //get message
                String formatMessage = UtilsMessage.setPlayerColors(message, player.getName());
                //send message
                UtilsTell.sendMessage(player, formatMessage);
            }
        }
    }
    //use command tell
    public static boolean useCommandTell(String message, Player eventPlayer, Player receiver, String whomMessage){
        //get plugin
        FlectoneChat plugin = FlectoneChat.getInstance();
        //get event player name and receiver name
        String eventPlayerName = eventPlayer.getName();
        String receiverName = receiver.getName();
        //get flectone player from receiver name
        FlectonePlayer flectonePlayerReceiver = plugin.allPlayers.get(receiverName);
        //if receiver ignore event player
        if(flectonePlayerReceiver.getIgnoreList().contains(eventPlayer.getUniqueId().toString())){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.you-ignored");
            return true;
        }
        //get flectone player from sender name
        FlectonePlayer flectonePlayerSender = plugin.allPlayers.get(eventPlayerName);
        //if event player ignore receiver
        if(flectonePlayerSender.getIgnoreList().contains(receiver.getUniqueId().toString())){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.you-ignore");
            return true;
        }
        //get format string
        String formatString = UtilsMain.getLanguageString("tell." + whomMessage + ".message");
        formatString = UtilsMessage.setPlayerColors(formatString, eventPlayerName);
        formatString = UtilsMessage.replacePlayerName(formatString, receiverName);
        //get click message
        String clickMessage = UtilsMain.getLanguageString("click.message");
        clickMessage = UtilsMessage.setPlayerColors(clickMessage, eventPlayerName);
        clickMessage = UtilsMessage.replacePlayerName(clickMessage, receiverName);
        //create text component
        TextComponent formatComponent = new TextComponent(TextComponent.fromLegacyText(formatString));
        //set click event for text component
        UtilsMessage.setClickEvent("/actions " + receiverName, clickMessage,formatComponent, eventPlayer, receiver);
        //new component builder
        ComponentBuilder formatBuilder = new ComponentBuilder();
        formatBuilder.append(formatComponent);
        //set color for message
        message = UtilsMessage.setPlayerColors(message, eventPlayerName);
        //add to builder
        formatBuilder.append(TextComponent.fromLegacyText(message), ComponentBuilder.FormatRetention.NONE);
        //send message
        eventPlayer.spigot().sendMessage(formatBuilder.create());
        //save last sender
        plugin.allPlayers.get(receiverName).setLastSender(eventPlayerName);
        return false;
    }
    //get message from language file and send
    public static void sendMessageLanguage(Player player, String stringLanguage){
        //get string
        String string = UtilsMain.getLanguageString(stringLanguage);
        //set color
        string = UtilsMessage.setPlayerColors(string, player.getName());
        //send message
        UtilsTell.sendMessage(player, string);
    }
    //check and send message myself
    public static void identicalPlayer(Player eventPlayer, String message){
        //get boolean
        if(!UtilsMain.getConfigBoolean("myself-message.enable")){
            //send message false
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.myself.false");
            return;
        }
        //get format true
        String formatString = UtilsMain.getLanguageString("tell.myself.true");
        formatString = formatString + message;
        formatString = UtilsMessage.setPlayerColors(formatString, eventPlayer.getName());
        //send message true
        UtilsTell.sendMessage(eventPlayer, formatString);
    }
    //simple send message
    public static void sendMessage(Player eventPlayer, String message){
        eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(message));
    }
}
