package ru.flectonechat.Tools.Utils;

import net.md_5.bungee.api.chat.TextComponent;
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

    public static ItemStack createHeadInventory(String headName, String playerName){
        ItemStack blockForHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) blockForHead.getItemMeta();
        skullMeta.setDisplayName(UtilsMain.formatString("&r" + headName));
        skullMeta.setOwner(playerName);
        blockForHead.setItemMeta(skullMeta);
        return blockForHead;
    }

    public static ItemStack createArrowInventory(ItemStack arrow, String arrowName){
        ItemMeta itemMeta = arrow.getItemMeta();
        arrowName = UtilsMain.formatString(arrowName);
        itemMeta.setDisplayName(arrowName);
        arrow.setItemMeta(itemMeta);
        return arrow;
    }

    public static void setIgnoreListInventory(FlectonePlayer flectonePlayer, HashMap ignoreListInventorys, boolean createNew){
        String flectonePlayerName = flectonePlayer.getPlayer().getName();
        String inventoryName = UtilsMessage.defaultLanguageString("ignorelist.inventory.name", flectonePlayerName);

        List<String> ignoreList = flectonePlayer.getIgnoreList();

        for(int x = 1; x < ignoreList.size() + 1; x++){

            int inventoryPage = x/9;

            Inventory inventory = (Inventory) ignoreListInventorys.get(inventoryPage);

            if(inventory == null || createNew){
                inventory = Bukkit.createInventory(null, 9*2, inventoryName + (inventoryPage+1));
                ignoreListInventorys.put(inventoryPage, inventory);
            }
        }

        for(int x = 0; x < ignoreList.size(); x++){
            UUID ignoredPlayerUUID = UUID.fromString(ignoreList.get(x));

            OfflinePlayer ignoredPlayer = Bukkit.getOfflinePlayer(ignoredPlayerUUID);
            ItemStack playerHead = UtilsGUI.createHeadInventory(ignoredPlayer.getName(), ignoredPlayer.getName());

            int numberItem = x;
            int inventoryPage = x/9;
            if(inventoryPage > 0){
                numberItem = x - 9*inventoryPage;
            }

            Inventory inventory = (Inventory) ignoreListInventorys.get(inventoryPage);

            inventory.setItem(numberItem, playerHead);

            if(inventoryPage != 0){
                String backArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.back_arrow", flectonePlayerName);
                ItemStack backArrow = new ItemStack(Material.ARROW);
                backArrow = UtilsGUI.createArrowInventory(backArrow, backArrowName);
                inventory.setItem(9, backArrow);
            }

            if(inventoryPage != (ignoreList.size()-1)/9){
                String nextArrowName = UtilsMessage.defaultLanguageString("ignorelist.inventory.next_arrow", flectonePlayerName);
                ItemStack nextArrow = new ItemStack(Material.SPECTRAL_ARROW);
                nextArrow = UtilsGUI.createArrowInventory(nextArrow, nextArrowName);
                inventory.setItem(17, nextArrow);
            }
        }
    }

    public static void actionsClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){

        Player clickedPlayer = flectonePlayer.getWhoPlayer();
        if(clickedPlayer == null) return;

        event.setCancelled(true);

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            return;
        }

        String afterMessage = UtilsMain.getLanguageString("actions.after_message");
        afterMessage = UtilsMain.formatString(afterMessage);
        afterMessage = UtilsMessage.replacePlayerName(afterMessage, clickedPlayer.getName());

        if(event.getSlot() == 11 || event.getSlot() == 13){
            flectonePlayer.setAfterMessage(clickedPlayer.getName());

            event.getWhoClicked().spigot().sendMessage(TextComponent.fromLegacyText(afterMessage));
            event.getWhoClicked().closeInventory();
        }

        if(event.getSlot() == 15){
            Bukkit.dispatchCommand(event.getWhoClicked(), "ignore " + clickedPlayer.getName());
        }
    }

    public static void ignoreListClick(InventoryClickEvent event, FlectonePlayer flectonePlayer){
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

            UtilsGUI.setIgnoreListInventory(flectonePlayer, ignoreListInventorys, false);
            flectonePlayer.setIgnoreListInventorys(ignoreListInventorys);

            ignoreListInventorys = flectonePlayer.getIgnoreListInventorys();
            Inventory inventory = (Inventory) ignoreListInventorys.get(numberPage);

            if(flectonePlayer.getIgnoreList().size() == 0){
                eventPlayer.closeInventory();
                return;
            }

            if(event.getSlot() == 0 && numberPage != 0){
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
