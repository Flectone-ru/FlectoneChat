package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsGUI;
import ru.flectonechat.Tools.Utils.UtilsTell;

import java.util.HashMap;
import java.util.List;

public class IgnoreList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //command: /ignorelist
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        //checking for args but this if impossible
        if(UtilsCommand.checkArgs(args, 0)){
            UtilsTell.sendMessageLanguage(eventPlayer, "ignorelist.usage");
            return true;
        }
        //get flectone player and he ignores list
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        List<String> ignoreList = plugin.ignoreFileConfig.getStringList(flectonePlayer.getPlayerUUID());
        //if player is not ignoring anyone
        if(ignoreList.size() == 0){
            UtilsTell.sendMessageLanguage(eventPlayer, "ignorelist.empty");
            return true;
        }
        //get flectone player hashmap with all inventories for ignore
        HashMap<Integer, Inventory> ignoreListInventories = flectonePlayer.getIgnoreListInventories();
        //create menu for command
        UtilsGUI.setIgnoreListInventory(flectonePlayer, ignoreListInventories, false);
        //again get
        ignoreListInventories = flectonePlayer.getIgnoreListInventories();
        //open and save
        eventPlayer.openInventory(ignoreListInventories.get(0));
        flectonePlayer.setIgnoreListInventories(ignoreListInventories);
        return true;
    }


}
