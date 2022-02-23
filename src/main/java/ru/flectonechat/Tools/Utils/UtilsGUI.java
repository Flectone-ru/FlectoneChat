package ru.flectonechat.Tools.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UtilsGUI {
    //create head for inventory
    public static ItemStack createHeadInventory(String headName, String playerName){

        ItemStack blockForHead = new ItemStack(Material.PLAYER_HEAD);
        //set custom meta
        SkullMeta skullMeta = (SkullMeta) blockForHead.getItemMeta();
        skullMeta.setDisplayName(UtilsMessage.formatString("&e" + headName.strip()));
        skullMeta.setOwner(playerName);
        blockForHead.setItemMeta(skullMeta);
        return blockForHead;
    }
    //create arrow for inventory
    public static ItemStack createArrowInventory(ItemStack arrow, String arrowName){

        ItemMeta itemMeta = arrow.getItemMeta();
        //set custom meta
        arrowName = UtilsMessage.formatString(arrowName);
        itemMeta.setDisplayName(arrowName);
        arrow.setItemMeta(itemMeta);
        return arrow;
    }
    //create ignore list inventory
    public static void setIgnoreListInventory(FlectonePlayer flectonePlayer, HashMap<Integer, Inventory> ignoreListInventories, boolean createNew){

        String flectonePlayerName = flectonePlayer.getPlayer().getName();

        String inventoryName = UtilsMessage.defaultLanguageString("ignorelist.inventory.name", flectonePlayerName);

        List<String> ignoreList = flectonePlayer.getIgnoreList();
        //create new inventory
        for(int x = 1; x < ignoreList.size() + 1; x++){
            //get inventories page
            int inventoryPage = x/9;
            //get inventory from hashmap
            Inventory inventory = ignoreListInventories.get(inventoryPage);

            if(inventory == null || createNew){

                inventory = Bukkit.createInventory(null, 9*2, inventoryName + (inventoryPage+1));
                ignoreListInventories.put(inventoryPage, inventory);
            }
        }
        //filling inventory
        for(int x = 0; x < ignoreList.size(); x++){

            UUID ignoredPlayerUUID = UUID.fromString(ignoreList.get(x));

            OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(ignoredPlayerUUID);

            ItemStack playerHead = UtilsGUI.createHeadInventory(ignoredPlayer.getName(), ignoredPlayer.getName());
            //number item for inventory
            int numberItem = x;
            //inventory page
            int inventoryPage = x/9;

            if(inventoryPage > 0){
                numberItem = x - 9*inventoryPage;
            }

            Inventory inventory = ignoreListInventories.get(inventoryPage);

            inventory.setItem(numberItem, playerHead);
            //create arrow for back inventory
            if(inventoryPage != 0){

                String backArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.back-arrow", flectonePlayerName);
                //create arrow
                ItemStack backArrow = new ItemStack(Material.ARROW);
                UtilsGUI.createArrowInventory(backArrow, backArrowName);

                inventory.setItem(9, backArrow);
            }
            //create arrow for next inventory
            if(inventoryPage != (ignoreList.size()-1)/9){

                String nextArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.next-arrow", flectonePlayerName);
                //create arrow
                ItemStack nextArrow = new ItemStack(Material.SPECTRAL_ARROW);
                UtilsGUI.createArrowInventory(nextArrow, nextArrowName);

                inventory.setItem(17, nextArrow);
            }
        }
    }
    //check player click when command /actions
    public static void actionsClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){

        Player clickedPlayer = flectonePlayer.getWhoPlayer();
        String clickedPlayerName = clickedPlayer.getName();

        event.setCancelled(true);
        //if player use swap for dupe
        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            return;
        }

        String afterMessage = UtilsMain.getLanguageString("actions.after-message");
        afterMessage = UtilsMessage.setPlayerColors(afterMessage, clickedPlayerName);
        afterMessage = UtilsMessage.replacePlayerName(afterMessage, clickedPlayerName);
        //if head for message
        if(event.getSlot() == 11 || event.getSlot() == 13){

            flectonePlayer.setAfterMessage(clickedPlayer.getName());

            UtilsTell.sendMessage((Player) event.getWhoClicked(), afterMessage);
            event.getWhoClicked().closeInventory();
        }
        //if head for ignore
        if(event.getSlot() == 15){
            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + clickedPlayer.getName());
        }
    }
    //check player click when command /ignorelist
    public static void ignoreListClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){

        Player eventPlayer = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();

        event.setCancelled(true);

        HashMap<Integer, Inventory> ignoreListInventories = flectonePlayer.getIgnoreListInventories();
        //get inventory page from event
        int numberPage = getNumberPage(ignoreListInventories, event);
        //open inventory with arrow
        switch(clickedItem.getType()){
            case ARROW -> {
                Inventory inventory = ignoreListInventories.get(numberPage-1);
                eventPlayer.openInventory(inventory);
                break;
            }
            case SPECTRAL_ARROW -> {
                Inventory inventory = ignoreListInventories.get(numberPage+1);
                eventPlayer.openInventory(inventory);
                break;
            }
            case PLAYER_HEAD -> {

                String ignoredPlayerName = clickedItem.getItemMeta().getDisplayName();
                ignoredPlayerName = ignoredPlayerName.substring(1).substring(1);

                Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + ignoredPlayerName);

                UtilsGUI.setIgnoreListInventory(flectonePlayer, ignoreListInventories, false);
                //save new menu
                flectonePlayer.setIgnoreListInventories(ignoreListInventories);

                Inventory inventory = ignoreListInventories.get(numberPage);

                if(flectonePlayer.getIgnoreList().size() == 0){
                    eventPlayer.closeInventory();
                    return;
                }
                //open back inventory if this null
                if(event.getSlot() == 0 && numberPage != 0 && inventory.getItem(1) == null){
                    eventPlayer.openInventory(ignoreListInventories.get(numberPage-1));
                    return;
                }
                eventPlayer.openInventory(inventory);
                break;
            }
        }
    }
    //get inventory page
    public static int getNumberPage(HashMap<Integer, Inventory> ignoreListInventories, InventoryClickEvent event){
        for(int x = 0; x < ignoreListInventories.size(); x++){
            Inventory ignoreListInventory = ignoreListInventories.get(x);
            if(event.getClickedInventory() == ignoreListInventory){
                return x;
            }
        }
        return -1;
    }
}
