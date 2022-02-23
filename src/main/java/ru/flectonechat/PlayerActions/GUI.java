package ru.flectonechat.PlayerActions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.UtilsGUI;

public class GUI implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){

        String eventPlayerName = event.getWhoClicked().getName();

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);

        if(event.getCurrentItem() == null) return;
        //open "actions" inventory
        if(event.getClickedInventory() == flectonePlayer.getLastClickedInventory()){
            UtilsGUI.actionsClick(event, flectonePlayer);
            return;
        }
        //open ignore list menu
        if(event.getClickedInventory().getSize() == 18){
            UtilsGUI.ignoreListClick(event, flectonePlayer);
        }
    }
}
