package ru.flectonechat.Tools.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsMessage {
    //create message from args and add color for word
    public static String createMessageFromArgs(String[] args, int firstArg, String color){
        String message = color;
        //create
        for(int x = firstArg; x < args.length; x++){
            message = message + args[x] + color + " ";
        }
        return message;
    }
    //search player name in message
    public static TextComponent searchPingMessage(String message, Player eventPlayer, Player player){
        //new text component
        TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(message));
        //get player name and player ping name
        String playerName = player.getName();
        String playerPing = checkPingPlayer(message);
        //if ping exist
        if(!playerPing.isEmpty()){
            //get color
            String pingColor = UtilsMain.getConfigString("player_ping.color");
            pingColor = UtilsMessage.setPlayerColors(pingColor, playerName);
            //set color
            message = pingColor + ChatColor.stripColor(message);
            //replace old text component
            messageComponent = new TextComponent(TextComponent.fromLegacyText(message));
            //get click message
            String clickMessage = UtilsMain.getLanguageString("click.message");
            //replace placeholder <player>
            clickMessage = replacePlayerName(clickMessage, playerPing);
            clickMessage = setPlayerColors(clickMessage, playerName);
            //set click event for message
            setClickEvent("/actions " + playerPing, clickMessage, messageComponent, eventPlayer, player);
        }
        return messageComponent;
    }
    //message contains player name or not
    public static String checkPingPlayer(String message){
        //get ping condition
        String pingCondition = UtilsMain.getConfigString("player_ping.condition");
        boolean playerPingEnable = UtilsMain.getConfigBoolean("player_ping.enable");
        //message to lower case;
        message = message.toLowerCase();
        //check this message
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            //if message contains player name
            if(message.contains(playerName.toLowerCase()) && message.contains(pingCondition) && playerPingEnable){
                return playerName;
            }
        }
        return "";
    }
    //method for message if event player used /actions and wrote message
    public static boolean checkAfterMessage(Player eventPlayer, String[] args){
        //get flectone player
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayer.getName());
        //after message not null
        if(!flectonePlayer.getAfterMessage().equals("not")){
            //create message from args and add color for word
            String message = UtilsMessage.createMessageFromArgs(args, 0, "<color_text>");
            //get player from his name
            Player player = Bukkit.getPlayer(flectonePlayer.getAfterMessage());
            //clear after message
            flectonePlayer.setAfterMessage("not");
            //if player left the game
            if(player == null){
                UtilsTell.sendMessageLanguage(eventPlayer, "actions.no-player");
                return true;
            }
            //if message for myself
            if(eventPlayer == player){
                //message for myself
                UtilsTell.identicalPlayer(eventPlayer, message);
                return true;
            }
            //send message for event player
            UtilsTell.useCommandTell(message, eventPlayer, player, "sender");
            //send message for receiver
            if(!plugin.allPlayers.get(player.getName()).getIgnoreList().contains(flectonePlayer.getPlayerUUID())){
                UtilsTell.useCommandTell(message, player, eventPlayer, "receiver");
            }
            return true;
        }
        return false;
    }
    //set click event for text component
    public static void setClickEvent(String command, String text, TextComponent textComponent, Player eventPlayer, Player player){
        //get boolean for myself message
        boolean mySelfMessageEnable = UtilsMain.getConfigBoolean("myself_message.enable");
        //format text
        text = formatString(text);
        //myself ping
        if(!mySelfMessageEnable && eventPlayer == player){
            return;
        }
        //set click and hover event
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(text))));
    }
    //get and format string from language
    public static String defaultLanguageString(String stringName, String playerName){
        //get string
        String string = UtilsMain.getLanguageString(stringName);
        //set color
        string = setPlayerColors(string, playerName);
        //replace placeholder <player>
        string = replacePlayerName(string, playerName);
        return string;
    }
    //set color for message
    public static String setPlayerColors(String string, String playerName){
        //get flectone player
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(playerName);
        //get themes list
        List<String> themesList = flectonePlayer.getThemesList();
        //replace placeholders <color_main> and <color_text>
        string = string.replace("<color_main>", themesList.get(0));
        string = string.replace("<color_text>", themesList.get(1));
        //format string
        string = formatString(string);
        return string;
    }
    //replace placeholder <player>
    public static String replacePlayerName(String string, String playerName){
        string = string.replace("<player>", playerName);
        return string;
    }
    //default color message to minecraft color
    public static String formatString(String string){

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String hexCode = string.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&" + c);
            }
            string = string.replace(hexCode, builder.toString());
            matcher = pattern.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    //replace placeholder <suffix> and <prefix>
    public static String replaceVaultPlaceholders(String message, Player player){
        //get flectone player
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(player.getName());
        //replace
        message = message.replace("<suffix>", flectonePlayer.getVaultSuffix());
        message = message.replace("<prefix>", flectonePlayer.getVaultPrefix());
        //update tab
        flectonePlayer.setWorldColor(player.getWorld());
        return message;
    }
}
