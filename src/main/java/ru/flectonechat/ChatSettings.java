package ru.flectonechat;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

import java.util.HashSet;
import java.util.List;

public class ChatSettings implements Listener {

    @EventHandler
    public void Chat(AsyncPlayerChatEvent event){
        //send message if after message not null
        if(UtilsMessage.checkAfterMessage(event.getPlayer(), event.getMessage().split(" "))){
            event.setCancelled(true);
            return;
        }
        //remove recipient if player ignore event player
        removeRecipientIgnore(event);
        //global chat
        if(UtilsMain.getConfigBoolean("chat.global.enable")){
            globalChat(event);
        }
        //local chat
        localChat(event);
        //message for console
        event.getRecipients().clear();
    }
    //remove recipient if player ignore event player
    private static void removeRecipientIgnore(AsyncPlayerChatEvent event){
        //get plugin
        FlectoneChat plugin = FlectoneChat.getInstance();
        for(Player player : new HashSet<>(event.getRecipients())){
            //get player ignore list
            List<String> recipientIgnoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();
            //remove recipient if player ignore event player
            if(recipientIgnoreList.contains(event.getPlayer().getUniqueId().toString())){
                event.getRecipients().remove(player);
            }
        }
    }
    //global chat
    private void globalChat(AsyncPlayerChatEvent event){
        //get condition
        String condition = UtilsMain.getConfigString("chat.global.condition");
        //get event message
        String eventMessage = event.getMessage();
        //event message start with condition
        if(eventMessage.startsWith(condition)){
            //remove first symbol
            eventMessage = eventMessage.substring(1);
            //remove first null symbol
            if(eventMessage.startsWith(" ")){
                eventMessage = eventMessage.substring(1);
            }
            //set message
            if(!eventMessage.isEmpty()){
                setMessage("global", event, eventMessage);
                event.getRecipients().clear();
                return;
            }
            //if event message equals "!"
            localChat(event);
        }
    }
    //local chat
    private void localChat(AsyncPlayerChatEvent event){
        //get event player
        Player eventPlayer = event.getPlayer();
        //remove recipient if player long away
        removeRecipientRange(event, eventPlayer.getLocation());
        //set message
        setMessage("local", event, event.getMessage());
        //if event not recipient
        checkRecipient(event.getRecipients().size(), eventPlayer);
        //clear all recipients
        event.getRecipients().clear();
    }
    //set message
    private void setMessage(String chatFormat, AsyncPlayerChatEvent event, String eventMessage){
        //get event player and his name
        Player eventPlayer = event.getPlayer();
        String eventPlayerName = eventPlayer.getName();
        //create message and send
        for(Player player : new HashSet<>(event.getRecipients())){
            //get message color
            String messageColor = UtilsMain.getConfigString("chat." + chatFormat + ".message");
            messageColor = UtilsMessage.setPlayerColors(messageColor, player.getName());
            //new message builder
            ComponentBuilder messageBuilder = new ComponentBuilder();
            //split event message
            for(String message : eventMessage.split(" ")){
                //add color to message
                message = messageColor + message;
                //search ping message and create text component
                TextComponent messageComponent = UtilsMessage.searchPingMessage(message, eventPlayer, player);
                //add to builder
                messageBuilder.append(messageComponent, ComponentBuilder.FormatRetention.NONE).append(" ");
            }
            //new format builder
            ComponentBuilder formatMessageBuilder = new ComponentBuilder();
            //get click message
            String clickMessage = UtilsMain.getLanguageString("click.message");
            clickMessage = UtilsMessage.setPlayerColors(clickMessage, player.getName());
            clickMessage = UtilsMessage.replacePlayerName(clickMessage, eventPlayerName);
            //get format message
            String formatMessage = UtilsMain.getConfigString("chat." + chatFormat + ".format");
            formatMessage = UtilsMessage.setPlayerColors(formatMessage, player.getName());
            formatMessage = UtilsMessage.replacePlayerName(formatMessage, eventPlayerName);
            //get format prefix
            String formatPrefix = UtilsMain.getConfigString("chat." + chatFormat + ".prefix");
            formatPrefix = UtilsMessage.setPlayerColors(formatPrefix, player.getName());
            //set format message
            formatMessage = formatPrefix + formatMessage;
            //create format component
            TextComponent formatMessageComponent = new TextComponent(TextComponent.fromLegacyText(formatMessage));
            //set click event
            UtilsMessage.setClickEvent("/actions " + eventPlayer.getName(), clickMessage, formatMessageComponent, eventPlayer, player);
            //add to format builder
            formatMessageBuilder.append(formatMessageComponent);
            formatMessageBuilder.append(messageBuilder.create(), ComponentBuilder.FormatRetention.NONE);
            //send message
            player.spigot().sendMessage(formatMessageBuilder.create());
        }

    }
    //check players in range
    private boolean playerInRange(Location receiverLocation, Location senderLocation, int range){
        if(receiverLocation.getWorld() != senderLocation.getWorld()){
            return false;
        }
        if(receiverLocation.distance(senderLocation) > range){
            return false;
        }
        return true;
    }
    //remove recipient if playerInRange false
    private void removeRecipientRange(AsyncPlayerChatEvent event, Location eventLocation){
        for (Player player : new HashSet<>(event.getRecipients())) {
            //get config int
            int range = UtilsMain.getConfigInt("chat.local.range");
            //remove recipient if playerInRange false
            if(!playerInRange(player.getLocation(), eventLocation, range)) {
                event.getRecipients().remove(player);
            }
        }
    }
    //if recipient size 1
    private void checkRecipient(Integer recipientSize, Player player){
        if(recipientSize == 1) {
            //send message
            UtilsTell.sendMessageLanguage(player, "chat.no-recipients");
        }
    }

}
