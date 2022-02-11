package ru.flectonechat.Tools;

import org.bukkit.World;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utils.UtilsMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlectonePlayer {
    FlectoneChat plugin = FlectoneChat.getInstance();

    private Player player;

    private String lastSender;
    private String playerUUID;
    private String afterMessage = "not";

    private List<String> ignoreList = new ArrayList<>();
    private List<String> themesList;

    private Inventory actionsInventory = null;
    private Inventory lastClickedInventory = null;

    private HashMap<Integer, Inventory> ignoreListInventorys = new HashMap<>();

    private Player whoPlayer;

    public void setPlayer(Player player) {
        this.player = player;

        this.playerUUID = player.getUniqueId().toString();


    }

    public Player getPlayer() {
        return player;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public String getLastSender() {
        return lastSender;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setIgnoreList(List<String> ignoreList) {
        this.ignoreList = ignoreList;
        plugin.ignoreFileConfig.set(playerUUID, this.ignoreList);

        try {
            plugin.ignoreFileConfig.save(plugin.ignoreFile);
        } catch (IOException e){

        }
    }

    public void setWorldColor(World world){
        if(UtilsMain.getConfigBoolean("world_color.enable")){
            String eventWorldName = world.getName();
            String worldColor = UtilsMain.getConfigString("world_color." + eventWorldName);

            worldColor = worldColor + player.getName();
            worldColor = UtilsMain.formatString(worldColor);

            player.setPlayerListName(worldColor);
        }
    }

    public void setThemesList(List<String> themesList){
        this.themesList = themesList;
        plugin.themesFileConfig.set(playerUUID, this.themesList);

        try {
            plugin.themesFileConfig.save(plugin.themesFile);
        } catch (IOException e){

        }

    }

    public List<String> getThemesList(){
        return themesList;
    }

    public void setActionsInventory(Inventory actionsInventory) {
        this.actionsInventory = actionsInventory;
    }

    public Inventory getActionsInventory() {
        return actionsInventory;
    }

    public void setLastClickedInventory(Inventory lastClickedInventory, Player whoPlayer) {
        this.whoPlayer = whoPlayer;
        this.lastClickedInventory = lastClickedInventory;
    }

    public Inventory getLastClickedInventory() {
        return lastClickedInventory;
    }

    public void setAfterMessage(String afterMessage) {
        this.afterMessage = afterMessage;
    }

    public String getAfterMessage() {
        return afterMessage;
    }

    public Player getWhoPlayer() {
        return whoPlayer;
    }

    public List<String> getIgnoreList() {
        return plugin.ignoreFileConfig.getStringList(playerUUID);
    }

    public void setIgnoreListInventorys(HashMap<Integer, Inventory> ignoreListInventorys) {
        this.ignoreListInventorys = ignoreListInventorys;
    }

    public HashMap<Integer, Inventory> getIgnoreListInventorys() {
        return ignoreListInventorys;
    }
}
