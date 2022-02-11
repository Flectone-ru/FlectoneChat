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
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "ignore.usage");
            return true;
        }

        OfflinePlayer mutPlayer = Bukkit.getOfflinePlayer(args[0]);
        if(mutPlayer.getName() == eventPlayerName){
            UtilsTell.sendErrorMessage(eventPlayer, "ignore.myself");
            return true;
        }

        if(!mutPlayer.hasPlayedBefore() && !Bukkit.getOnlinePlayers().contains(mutPlayer)){
            UtilsTell.sendErrorMessage(eventPlayer, "ignore.no_player");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        List<String> ignoreList = flectonePlayer.getIgnoreList();
        if(ignoreList.size() == 0){
            ignoreList = new ArrayList<>();
        }

        String mutPlayerUUID = mutPlayer.getUniqueId().toString();

        if(ignoreList.contains(mutPlayerUUID)){
            ignoreList.remove(mutPlayerUUID);
            flectonePlayer.setIgnoreList(ignoreList);

            UtilsGUI.setIgnoreListInventory(flectonePlayer, flectonePlayer.getIgnoreListInventorys(), true);

            sendMessage("un_mute", eventPlayer, args[0]);
            return true;
        }

        ignoreList.add(mutPlayerUUID);
        flectonePlayer.setIgnoreList(ignoreList);

        sendMessage("mute", eventPlayer, args[0]);

        return true;
    }

    private void sendMessage(String format, Player eventPlayer, String mutPlayerName){
        String formatMessage = UtilsMain.getLanguageString("ignore." + format);
        formatMessage = UtilsMain.formatString(formatMessage);
        formatMessage = UtilsMessage.replacePlayerName(formatMessage, mutPlayerName);
        UtilsTell.spigotMessage(eventPlayer, formatMessage);
    }
}
