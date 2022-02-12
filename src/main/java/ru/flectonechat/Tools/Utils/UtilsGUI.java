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
        //create head
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
        //create arrow
        ItemMeta itemMeta = arrow.getItemMeta();
        //set custom meta
        arrowName = UtilsMessage.formatString(arrowName);
        itemMeta.setDisplayName(arrowName);
        arrow.setItemMeta(itemMeta);
        return arrow;
    }
    //create ignore list inventory
    public static void setIgnoreListInventory(FlectonePlayer flectonePlayer, HashMap<Integer, Inventory> ignoreListInventories, boolean createNew){
        //get player name
        String flectonePlayerName = flectonePlayer.getPlayer().getName();
        //get inventory name
        String inventoryName = UtilsMessage.defaultLanguageString("ignorelist.inventory.name", flectonePlayerName);
        //get ignore list
        List<String> ignoreList = flectonePlayer.getIgnoreList();
        //create new inventory
        for(int x = 1; x < ignoreList.size() + 1; x++){
            //get inventories page
            int inventoryPage = x/9;
            //get inventory from hashmap
            Inventory inventory = ignoreListInventories.get(inventoryPage);
            //create inventory and save
            if(inventory == null || createNew){
                //create
                inventory = Bukkit.createInventory(null, 9*2, inventoryName + (inventoryPage+1));
                //save
                ignoreListInventories.put(inventoryPage, inventory);
            }
        }
        //filling inventory
        for(int x = 0; x < ignoreList.size(); x++){
            //get uuid
            UUID ignoredPlayerUUID = UUID.fromString(ignoreList.get(x));
            //get offline player because player may be offline
            OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(ignoredPlayerUUID);
            //create offline player head
            ItemStack playerHead = UtilsGUI.createHeadInventory(ignoredPlayer.getName(), ignoredPlayer.getName());
            //number item for inventory
            int numberItem = x;
            //inventory page
            int inventoryPage = x/9;
            //if x>9 then reset number item
            if(inventoryPage > 0){
                numberItem = x - 9*inventoryPage;
            }
            //get inventory from hashmap
            Inventory inventory = ignoreListInventories.get(inventoryPage);
            //set head in inventory
            inventory.setItem(numberItem, playerHead);
            //create arrow for back inventory
            if(inventoryPage != 0){
                //get name
                String backArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.back-arrow", flectonePlayerName);
                //create arrow
                ItemStack backArrow = new ItemStack(Material.ARROW);
                UtilsGUI.createArrowInventory(backArrow, backArrowName);
                //set
                inventory.setItem(9, backArrow);
            }
            //create arrow for next inventory
            if(inventoryPage != (ignoreList.size()-1)/9){
                //get name
                String nextArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.next-arrow", flectonePlayerName);
                //create arrow
                ItemStack nextArrow = new ItemStack(Material.SPECTRAL_ARROW);
                UtilsGUI.createArrowInventory(nextArrow, nextArrowName);
                //set
                inventory.setItem(17, nextArrow);
            }
        }
    }
    //check player click when command /actions
    public static void actionsClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){
        //get clicked player
        Player clickedPlayer = flectonePlayer.getWhoPlayer();
        String clickedPlayerName = clickedPlayer.getName();
        //cancel click
        event.setCancelled(true);
        //if player use swap for dupe
        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            return;
        }
        //get message from file
        String afterMessage = UtilsMain.getLanguageString("actions.after-message");
        afterMessage = UtilsMessage.setPlayerColors(afterMessage, clickedPlayerName);
        afterMessage = UtilsMessage.replacePlayerName(afterMessage, clickedPlayerName);
        //if head for message
        if(event.getSlot() == 11 || event.getSlot() == 13){
            //set value "after message" for check it in chat
            flectonePlayer.setAfterMessage(clickedPlayer.getName());
            //send message
            UtilsTell.sendMessage((Player) event.getWhoClicked(), afterMessage);
            event.getWhoClicked().closeInventory();
        }
        //if head for ignore
        if(event.getSlot() == 15){
            //use command
            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + clickedPlayer.getName());
        }
    }
    //check player click when command /ignorelist
    public static void ignoreListClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){
        //get player
        Player eventPlayer = (Player) event.getWhoClicked();
        //get item
        ItemStack clickedItem = event.getCurrentItem();
        //cancel click
        event.setCancelled(true);
        //get hashmap from flectone player
        HashMap<Integer, Inventory> ignoreListInventories = flectonePlayer.getIgnoreListInventories();
        //get inventory page from event
        int numberPage = getNumberPage(ignoreListInventories, event);
        //if item for back inventory
        if(clickedItem.getType() == Material.ARROW){
            //get and open inventory
            Inventory inventory = ignoreListInventories.get(numberPage-1);
            eventPlayer.openInventory(inventory);
            return;
        }
        //if item for next inventory
        if(clickedItem.getType() == Material.SPECTRAL_ARROW){
            //get and open inventory
            Inventory inventory = ignoreListInventories.get(numberPage+1);
            eventPlayer.openInventory(inventory);
            return;
        }
        //if item for unignore
        if(clickedItem.getType() == Material.PLAYER_HEAD){
            //get ignored name from item
            String ignoredPlayerName = clickedItem.getItemMeta().getDisplayName();
            ignoredPlayerName = ignoredPlayerName.substring(1).substring(1);
            //use command /ignore
            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + ignoredPlayerName);
            //create new inventory menu
            UtilsGUI.setIgnoreListInventory(flectonePlayer, ignoreListInventories, false);
            //save new menu
            flectonePlayer.setIgnoreListInventories(ignoreListInventories);
            //get inventory from hashmap
            Inventory inventory = ignoreListInventories.get(numberPage);
            //if player ignore list null
            if(flectonePlayer.getIgnoreList().size() == 0){
                eventPlayer.closeInventory();
                return;
            }
            //open back inventory if this null
            if(event.getSlot() == 0 && numberPage != 0 && inventory.getItem(1) == null){
                eventPlayer.openInventory(ignoreListInventories.get(numberPage-1));
                return;
            }
            //open inventory
            eventPlayer.openInventory(inventory);
        }
    }
    //get inventory page
    public static int getNumberPage(HashMap<Integer, Inventory> ignoreListInventories, InventoryClickEvent event){
        for(int x = 0; x < ignoreListInventories.size(); x++){
            //get inventory
            Inventory ignoreListInventory = ignoreListInventories.get(x);
            //if page found
            if(event.getClickedInventory() == ignoreListInventory){
                return x;
            }
        }
        return -1;
    }
}
