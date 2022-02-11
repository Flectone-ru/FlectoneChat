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

public class UtilsMessage {

    public static String createMessageFromArgs(String[] args, int firstArg, String color){
        String message = color;
        for(int x = firstArg; x < args.length; x++){
            message = message + args[x] + color + " ";
        }
        return message;
    }

    public static TextComponent searchPingMessage(String message, Player eventPlayer, Player player){

        TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(message));

        String playerName = player.getName();
        String playerPing = checkPingPlayer(message);

        if(!playerPing.isEmpty()){
            String pingColor = UtilsMain.getConfigString("player_ping.color");
            pingColor = UtilsMessage.setPlayerColors(pingColor, eventPlayer.getName());

            message = pingColor + ChatColor.stripColor(message);

            messageComponent = new TextComponent(TextComponent.fromLegacyText(message));

            String clickMessage = UtilsMain.getLanguageString("click.message");
            clickMessage = replacePlayerName(clickMessage, playerPing);
            clickMessage = setPlayerColors(clickMessage, playerName);

            setClickEvent("/actions " + playerPing, clickMessage, messageComponent, eventPlayer, player);
        }

        return messageComponent;
    }

    public static String checkPingPlayer(String message){
        String pingCondition = UtilsMain.getConfigString("player_ping.condition");
        boolean playerPingEnable = UtilsMain.getConfigBoolean("player_ping.enable");
        message = message.toLowerCase();

        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            if(message.contains(playerName.toLowerCase()) && message.contains(pingCondition) && playerPingEnable){
                return playerName;
            }
        }
        return "";
    }

    public static boolean checkAfterMessage(Player eventPlayer, String[] args){
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayer.getName());
        if(!flectonePlayer.getAfterMessage().equals("not")){

            String message = UtilsMessage.createMessageFromArgs(args, 0, "<color_text>");

            Player player = UtilsMain.checkPlayerOnServer(flectonePlayer.getAfterMessage());

            flectonePlayer.setAfterMessage("not");

            if(player == null){
                UtilsTell.sendErrorMessage(eventPlayer, "actions.no_player");
                return true;
            }
            if(eventPlayer == player){
                UtilsTell.identicalPlayer(eventPlayer, message);
                return true;
            }

            UtilsTell.useCommandTell(message, eventPlayer, player, "sender");
            UtilsTell.useCommandTell(message, player, eventPlayer, "receiver");
            return true;
        }
        return false;
    }

    public static void setClickEvent(String command, String text, TextComponent textComponent, Player eventPlayer, Player player){
        boolean mySelfMessageEnable = UtilsMain.getConfigBoolean("myself_message.enable");

        text = UtilsMain.formatString(text);

        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(text))));

        if(!mySelfMessageEnable && eventPlayer == player){
            textComponent = new TextComponent(TextComponent.fromLegacyText(text));
        }
    }

    public static String defaultLanguageString(String stringName, String playerName){
        String string = UtilsMain.getLanguageString(stringName);
        string = setPlayerColors(string, playerName);
        string = replacePlayerName(string, playerName);
        return string;
    }

    public static String setPlayerColors(String string, String playerName){
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(playerName);

        List<String> themesList = flectonePlayer.getThemesList();

        string = string.replace("<color_main>", themesList.get(0));
        string = string.replace("<color_text>", themesList.get(1));

        string = UtilsMain.formatString(string);

        return string;
    }

    public static String replacePlayerName(String string, String playerName){
        string = string.replace("<player>", playerName);
        return string;
    }
}
