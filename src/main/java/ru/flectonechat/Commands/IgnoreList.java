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
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        if(UtilsCommand.checkArgs(args, 0)){
            UtilsTell.sendErrorMessage(eventPlayer, "ignorelist.usage");
            return true;
        }
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);

        List<String> ignoreList = plugin.ignoreFileConfig.getStringList(flectonePlayer.getPlayerUUID());

        if(ignoreList.size() == 0){
            UtilsTell.sendErrorMessage(eventPlayer, "ignorelist.empty");
            return true;
        }

        HashMap ignoreListInventorys = flectonePlayer.getIgnoreListInventorys();

        UtilsGUI.setIgnoreListInventory(flectonePlayer, ignoreListInventorys, false);

        ignoreListInventorys = flectonePlayer.getIgnoreListInventorys();
        eventPlayer.openInventory((Inventory) ignoreListInventorys.get(0));
        flectonePlayer.setIgnoreListInventorys(ignoreListInventorys);

        return true;
    }


}
