package ru.flectonechat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.*;

import java.util.ArrayList;
import java.util.List;

public class Ignore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!UtilsMain.senderIsPlayer(sender)) return true;
        //command: /ignore (player)
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        //checking for args
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendMessageLanguage(eventPlayer, "ignore.usage");
            return true;
        }

        OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(args[0]);

        if(ignoredPlayer.getName().equals(eventPlayerName)){
            UtilsTell.sendMessageLanguage(eventPlayer, "ignore.myself");
            return true;
        }
        //if player doesn't exist
        if(!ignoredPlayer.hasPlayedBefore() && !Bukkit.getOnlinePlayers().contains(ignoredPlayer)){
            UtilsTell.sendMessageLanguage(eventPlayer, "ignore.no-player");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);

        List<String> ignoreList = flectonePlayer.getIgnoreList();
        if(ignoreList.size() == 0){
            ignoreList = new ArrayList<>();
        }
        String mutPlayerUUID = ignoredPlayer.getUniqueId().toString();
        //if ignored player already in list
        if(ignoreList.contains(mutPlayerUUID)){

            ignoreList.remove(mutPlayerUUID);
            flectonePlayer.setIgnoreList(ignoreList);
            //create inventory for command: /ignorelist
            UtilsGUI.setIgnoreListInventory(flectonePlayer, flectonePlayer.getIgnoreListInventories(), true);

            sendMessage("un-mute", eventPlayer, args[0]);
            return true;
        }

        ignoreList.add(mutPlayerUUID);
        flectonePlayer.setIgnoreList(ignoreList);

        sendMessage("mute", eventPlayer, args[0]);
        return true;
    }
    //simple method for send message
    private void sendMessage(String format, Player eventPlayer, String mutPlayerName){
        String formatMessage = UtilsMain.getLanguageString("ignore." + format);
        formatMessage = UtilsMessage.setPlayerColors(formatMessage, eventPlayer.getName());
        formatMessage = UtilsMessage.replacePlayerName(formatMessage, mutPlayerName);
        UtilsTell.sendMessage(eventPlayer, formatMessage);
    }
}
