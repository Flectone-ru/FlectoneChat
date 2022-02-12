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
        //commands: /actions (player)
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        //checking for args
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendMessageLanguage(eventPlayer, "actions.usage");
            return true;
        }
        //get player from name
        Player clickedPlayer = Bukkit.getPlayer(args[0]);
        //if player doesn't exist
        if(clickedPlayer == null){
            UtilsTell.sendMessageLanguage(eventPlayer, "actions.no-player");
            return true;
        }
        //get "actions" inventory and flectonePlayer
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(clickedPlayer.getName());
        Inventory actionsInventory = flectonePlayer.getActionsInventory();
        //create new "actions" inventory
        if(actionsInventory == null){
            //get main string
            String clickedPlayerName = clickedPlayer.getName();
            String inventoryName = UtilsMessage.defaultLanguageString("actions.name", clickedPlayerName);
            String headIgnore = UtilsMessage.defaultLanguageString("actions.head.ignore", clickedPlayerName);
            String headMessage = UtilsMessage.defaultLanguageString("actions.head.message", clickedPlayerName);
            //create minecraft inventory
            actionsInventory = Bukkit.createInventory(null, 9*3, inventoryName);
            //create item for inventory
            ItemStack headForIgnore = UtilsGUI.createHeadInventory(headIgnore, clickedPlayerName);
            ItemStack headForMessage = UtilsGUI.createHeadInventory(headMessage, "ElMarcosFTW");
            //check clicked player == event player
            if(clickedPlayer.getName().equals(eventPlayerName)){
                //set one head for message
                actionsInventory.setItem(13, headForMessage);
            } else {
                //set heads for ignore and message
                actionsInventory.setItem(11, headForMessage);
                actionsInventory.setItem(15, headForIgnore);
            }
            //save "actions" inventory for clicked player
            flectonePlayer.setActionsInventory(actionsInventory);
        }
        eventPlayer.openInventory(actionsInventory);
        //save inventory for event player
        FlectonePlayer flectoneEventPlayer = plugin.allPlayers.get(eventPlayerName);
        flectoneEventPlayer.setLastClickedInventory(actionsInventory, clickedPlayer);
        return true;
    }
}
