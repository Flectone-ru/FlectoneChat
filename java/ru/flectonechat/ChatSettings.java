package ru.flectonechat;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

import java.util.HashSet;
import java.util.List;

public class ChatSettings implements Listener {

    @EventHandler
    public void Chat(AsyncPlayerChatEvent event){

        if(checkAfterMessage(event.getPlayer(), event.getMessage().split(" "))){
            event.setCancelled(true);
            return;
        }

        boolean enableGlobalChat = Utilities.getConfigBoolean("chat.global.enable");

        removeRecipientIgnore(event);

        localChat(event);
        event.getRecipients().clear();

    }

    private static void removeRecipientIgnore(AsyncPlayerChatEvent event){
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(event.getPlayer().getName());
        for(Player player : new HashSet<>(event.getRecipients())){
            List<String> recipientIgnoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();

            if(recipientIgnoreList.contains(event.getPlayer().getUniqueId().toString())){
                event.getRecipients().remove(player);
            }
        }
    }

    private void globalChat(AsyncPlayerChatEvent event){
        String condition = Utilities.getConfigString("chat.global.condition");
        String eventMessage = event.getMessage();

        if(eventMessage.startsWith(condition)){
            eventMessage = eventMessage.substring(1);
            if(eventMessage.startsWith(" ")){
                eventMessage = eventMessage.substring(1);
            }

            if(!eventMessage.isEmpty()){
                setMessage("global", event, eventMessage);
                event.getRecipients().clear();
                return;
            }

            localChat(event);

        }
    }

    private void localChat(AsyncPlayerChatEvent event){
        Player eventPlayer = event.getPlayer();

        for (Player player : new HashSet<>(event.getRecipients())) {
            int range = Utilities.getConfigInt("chat.local.range");

            if(!playerInRange(player.getLocation(), eventPlayer.getLocation(), range)) {
                event.getRecipients().remove(player);
            }
        }

        setMessage("local", event, event.getMessage());

        checkRecipient(event.getRecipients().size(), eventPlayer);

        event.getRecipients().clear();
        return;
    }

    private void setMessage(String chatFormat, AsyncPlayerChatEvent event, String eventMessage){
        Player eventPlayer = event.getPlayer();
        String eventPlayerName = eventPlayer.getName();

        //send message
        for(Player player : new HashSet<>(event.getRecipients())){

            String messageColor = Utilities.getConfigString("chat." + chatFormat + ".message");
            messageColor = Utilities.setPlayerColors(messageColor, player.getName());

            ComponentBuilder messageBuilder = new ComponentBuilder();

            //split event message
            for(String message : eventMessage.split(" ")){

                message = messageColor + message;
                TextComponent messageComponent = Utilities.searchPingMessage(message, eventPlayer, player);

                messageBuilder.append(messageComponent, ComponentBuilder.FormatRetention.NONE).append(" ");
            }

            ComponentBuilder formatMessageBuilder = new ComponentBuilder();

            String clickMessage = Utilities.getLanguageString("click.message");
            clickMessage = Utilities.setPlayerColors(clickMessage, player.getName())
                    .replace("<player>", eventPlayerName);

            //set format
            String formatMessage = Utilities.getConfigString("chat." + chatFormat + ".format");
            formatMessage = Utilities.setPlayerColors(formatMessage, player.getName())
                    .replace("<player>", eventPlayer.getName());

            String formatPrefix = Utilities.getConfigString("chat." + chatFormat + ".prefix");
            formatPrefix = Utilities.setPlayerColors(formatPrefix, player.getName());

            formatMessage = formatPrefix + formatMessage;

            TextComponent formatMessageComponent = new TextComponent(TextComponent.fromLegacyText(formatMessage));

            //set click event
            Utilities.setClickEvent("/actions " + eventPlayer.getName(), clickMessage, formatMessageComponent, eventPlayer, player);

            formatMessageBuilder.append(formatMessageComponent);
            formatMessageBuilder.append(messageBuilder.create(), ComponentBuilder.FormatRetention.NONE);
            player.spigot().sendMessage(formatMessageBuilder.create());
        }

    }

    private boolean playerInRange(Location receiverLocation, Location senderLocation, int range){
        if(receiverLocation.getWorld() != senderLocation.getWorld()){
            return false;
        }
        if(receiverLocation.distance(senderLocation) > range){
            return false;
        }
        return true;
    }

    private void checkRecipient(Integer recipientSize, Player player){
        if(recipientSize == 1) {
            String noRecipients = Utilities.getLanguageString("chat.no_recipients");
            noRecipients = Utilities.formatString(noRecipients);

            player.spigot().sendMessage(TextComponent.fromLegacyText(noRecipients));
        }
    }

    private boolean checkAfterMessage(Player eventPlayer, String[] args){
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayer.getName());
        if(!flectonePlayer.getAfterMessage().equals("not")){

            String message = Utilities.createMessageFromArgs(args, 0, "<color_text>");

            Player player = Utilities.checkPlayerOnServer(flectonePlayer.getAfterMessage());

            flectonePlayer.setAfterMessage("not");

            if(player == null){
                Utilities.sendErrorMessage(eventPlayer, "actions.no_player");
                return true;
            }
            Utilities.useCommandTell(message, eventPlayer, player, "sender");
            Utilities.useCommandTell(message, player, eventPlayer, "receiver");
            return true;
        }
        return false;
    }

}
