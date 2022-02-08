package ru.flectonechat.PlayerActions;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

import java.util.HashMap;

public class GUI implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){

        String eventPlayerName = event.getWhoClicked().getName();

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);

        if(event.getCurrentItem() == null) return;

        if(event.getClickedInventory() == flectonePlayer.getLastClickedInventory()){
            actionsClick(event, flectonePlayer);
            return;
        }

        if(event.getClickedInventory().getSize() == 18){
            ignoreListClick(event, flectonePlayer);
            return;
        }
    }

    private void actionsClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){

        Player clickedPlayer = flectonePlayer.getWhoPlayer();
        if(clickedPlayer == null) return;

        event.setCancelled(true);

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            return;
        }

        String afterMessage = Utilities.getLanguageString("actions.after_message");
        afterMessage = Utilities.formatString(afterMessage).replace("<player>", clickedPlayer.getName());

        if(event.getSlot() == 11 || event.getSlot() == 13){
            flectonePlayer.setAfterMessage(clickedPlayer.getName());

            event.getWhoClicked().spigot().sendMessage(TextComponent.fromLegacyText(afterMessage));
            event.getWhoClicked().closeInventory();
        }

        if(event.getSlot() == 15){
            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + clickedPlayer.getName());
        }
    }

    private void ignoreListClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){
        Player eventPlayer = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        event.setCancelled(true);

        HashMap ignoreListInventorys = flectonePlayer.getIgnoreListInventorys();
        int numberPage = getNumberPage(ignoreListInventorys, event);
        if(clickedItem.getType() == Material.ARROW){
            Inventory inventory = (Inventory) ignoreListInventorys.get(numberPage-1);
            eventPlayer.openInventory(inventory);
            return;
        }
        if(clickedItem.getType() == Material.SPECTRAL_ARROW){
            Inventory inventory = (Inventory) ignoreListInventorys.get(numberPage+1);
            eventPlayer.openInventory(inventory);
            return;
        }

        if(clickedItem.getType() == Material.PLAYER_HEAD){
            String ignoredPlayerName = clickedItem.getItemMeta().getDisplayName();

            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + ignoredPlayerName);

            Utilities.setIgnoreListInventory(flectonePlayer, ignoreListInventorys, false);
            flectonePlayer.setIgnoreListInventorys(ignoreListInventorys);

            ignoreListInventorys = flectonePlayer.getIgnoreListInventorys();
            Inventory inventory = (Inventory) ignoreListInventorys.get(numberPage);

            if(flectonePlayer.getIgnoreList().size() == 0){
                eventPlayer.closeInventory();
                return;
            }

            if(event.getSlot() == 0){
                eventPlayer.openInventory((Inventory) ignoreListInventorys.get(numberPage-1));
                return;
            }

            eventPlayer.openInventory(inventory);

            return;
        }
    }

    public static int getNumberPage(HashMap ignoreListInventorys, InventoryClickEvent event){
        for(int x = 0; x < ignoreListInventorys.size(); x++){
            Inventory ignoreListInventory = (Inventory) ignoreListInventorys.get(x);
            if(event.getClickedInventory() == ignoreListInventory){
                return x;
            }
        }
        return -1;
    }
}
