package ru.flectonechat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.*;

public class Actions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();

        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "actions.usage");
            return true;
        }

        Player clickedPlayer = UtilsMain.checkPlayerOnServer(args[0]);
        if(clickedPlayer == null){
            UtilsTell.sendErrorMessage(eventPlayer, "actions.no_player");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(clickedPlayer.getName());

        Inventory actionsInventory = flectonePlayer.getActionsInventory();

        if(actionsInventory == null){
            String clickedPlayerName = clickedPlayer.getName();

            String inventoryName = UtilsMessage.defaultLanguageString("actions.name", clickedPlayerName);
            String headIgnore = UtilsMessage.defaultLanguageString("actions.head.ignore", clickedPlayerName);
            String headMessage = UtilsMessage.defaultLanguageString("actions.head.message", clickedPlayerName);

            actionsInventory = Bukkit.createInventory(null, 9*3, inventoryName);

            ItemStack headForIgnore = UtilsGUI.createHeadInventory(headIgnore, clickedPlayerName);
            ItemStack headForMessage = UtilsGUI.createHeadInventory(headMessage, "ElMarcosFTW");

            if(clickedPlayer.getName().equals(eventPlayerName)){
                actionsInventory.setItem(13, headForMessage);
            } else {
                actionsInventory.setItem(11, headForMessage);
                actionsInventory.setItem(15, headForIgnore);
            }

            flectonePlayer.setActionsInventory(actionsInventory);
        }

        eventPlayer.openInventory(actionsInventory);
        FlectonePlayer flectoneEventPlayer = plugin.allPlayers.get(eventPlayerName);
        flectoneEventPlayer.setLastClickedInventory(actionsInventory, clickedPlayer);

        return true;
    }
}
