package ru.flectonechat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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

        if(UtilsMain.getConfigBoolean("chat.global.enable")){
            globalChat(event);
        }

        localChat(event);
        //message for console
        event.getRecipients().clear();
    }
    //remove recipient if player ignore event player
    private static void removeRecipientIgnore(AsyncPlayerChatEvent event){

        FlectoneChat plugin = FlectoneChat.getInstance();
        for(Player player : new HashSet<>(event.getRecipients())){

            List<String> recipientIgnoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();
            //remove recipient if player ignore event player
            if(recipientIgnoreList.contains(event.getPlayer().getUniqueId().toString())){
                event.getRecipients().remove(player);
            }
        }
    }
    //global chat
    private void globalChat(AsyncPlayerChatEvent event){

        String condition = UtilsMain.getConfigString("chat.global.condition");

        String eventMessage = event.getMessage();

        if(eventMessage.startsWith(condition)){

            eventMessage = eventMessage.substring(1);

            if(eventMessage.startsWith(" ")){
                eventMessage = eventMessage.substring(1);
            }
            //set message
            if(!eventMessage.isEmpty()){
                setMessage("global", event, eventMessage);
                event.getRecipients().clear();
                return;
            }

            localChat(event);
        }
    }
    //local chat
    private void localChat(AsyncPlayerChatEvent event){

        Player eventPlayer = event.getPlayer();
        //remove recipient if player long away
        if(UtilsMain.getConfigBoolean("chat.global.enable")){
            removeRecipientRange(event, eventPlayer.getLocation());
        }

        setMessage("local", event, event.getMessage());

        checkRecipient(event.getRecipients().size(), eventPlayer);
        event.getRecipients().clear();
    }
    //set message
    private void setMessage(String chatFormat, AsyncPlayerChatEvent event, String eventMessage){

        Player eventPlayer = event.getPlayer();
        String eventPlayerName = eventPlayer.getName();
        //create message and send
        for(Player player : new HashSet<>(event.getRecipients())){

            String messageColor = UtilsMain.getConfigString("chat." + chatFormat + ".message");
            messageColor = UtilsMessage.setPlayerColors(messageColor, player.getName());

            ComponentBuilder messageBuilder = new ComponentBuilder();
            //split event message
            for(String message : eventMessage.split(" ")){
                //add color to message
                message = messageColor + message;
                //search ping message and create text component
                TextComponent messageComponent = UtilsMessage.searchPingMessage(message, eventPlayer, player);

                messageBuilder.append(messageComponent, ComponentBuilder.FormatRetention.NONE).append(" ");
            }

            ComponentBuilder formatMessageBuilder = new ComponentBuilder();

            String clickMessage = UtilsMain.getLanguageString("click.message");
            clickMessage = UtilsMessage.setPlayerColors(clickMessage, player.getName());
            clickMessage = UtilsMessage.replacePlayerName(clickMessage, eventPlayerName);

            String formatMessage = UtilsMain.getConfigString("chat." + chatFormat + ".format");
            formatMessage = UtilsMessage.setPlayerColors(formatMessage, player.getName());
            formatMessage = UtilsMessage.replacePlayerName(formatMessage, eventPlayerName);

            formatMessage = UtilsMessage.replaceVaultPlaceholders(formatMessage, eventPlayer);

            String formatPrefix = UtilsMain.getConfigString("chat." + chatFormat + ".prefix");
            formatPrefix = UtilsMessage.setPlayerColors(formatPrefix, player.getName());

            formatMessage = formatPrefix + formatMessage;

            TextComponent formatMessageComponent = new TextComponent(TextComponent.fromLegacyText(formatMessage));

            UtilsMessage.setClickEvent("/actions " + eventPlayer.getName(), clickMessage, formatMessageComponent, eventPlayer, player);

            formatMessageBuilder.append(formatMessageComponent);
            formatMessageBuilder.append(messageBuilder.create(), ComponentBuilder.FormatRetention.NONE);

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

            int range = UtilsMain.getConfigInt("chat.local.range");

            if(!playerInRange(player.getLocation(), eventLocation, range)) {
                event.getRecipients().remove(player);
            }
        }
    }
    //if recipient size 1
    private void checkRecipient(Integer recipientSize, Player player){
        if(recipientSize == 1) {
            UtilsTell.sendMessageLanguage(player, "chat.no-recipients");
        }
    }

}
