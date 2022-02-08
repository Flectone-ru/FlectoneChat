package ru.flectonechat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

public class Actions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "actions.usage");
            return true;
        }

        Player clickedPlayer = Utilities.checkPlayerOnServer(args[0]);
        if(clickedPlayer == null){
            Utilities.sendErrorMessage(eventPlayer, "actions.no_player");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(clickedPlayer.getName());

        org.bukkit.inventory.Inventory actionsInventory = flectonePlayer.getActionsInventory();

        if(actionsInventory == null){
            String inventoryName = Utilities.getLanguageString("actions.name");
            inventoryName = Utilities.formatString(inventoryName).replace("<player>", clickedPlayer.getName());

            String headIgnore = Utilities.getLanguageString("actions.head.ignore");
            headIgnore = Utilities.formatString(headIgnore).replace("<player>", args[0]);

            String headMessage = Utilities.getLanguageString("actions.head.message");
            headMessage = Utilities.formatString(headMessage).replace("<player>", args[0]);

            actionsInventory = Bukkit.createInventory(null, 9*3, inventoryName);

            ItemStack headForIgnore = Utilities.createHeadInventory(headIgnore, clickedPlayer.getName());
            ItemStack headForMessage = Utilities.createHeadInventory(headMessage, "ElMarcosFTW");

            if(clickedPlayer.getName().equals(eventPlayerName)){
                actionsInventory.setItem(13, headForMessage);
            } else {
                actionsInventory.setItem(11, headForMessage);
                actionsInventory.setItem(15, headForIgnore);
            }

            flectonePlayer.setActionsInventory(actionsInventory);
        }

        FlectonePlayer flectoneEventPlayer = plugin.allPlayers.get(eventPlayerName);
        flectoneEventPlayer.setLastClickedInventory(actionsInventory, clickedPlayer);

        return true;
    }
}
