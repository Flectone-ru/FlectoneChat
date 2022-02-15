package ru.flectonechat.Tools;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.World;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlectonePlayer {
    //get plugin
    FlectoneChat plugin = FlectoneChat.getInstance();
    //this player
    private Player player;
    //who is a player
    private Player whoPlayer;
    //last sender for player
    private String lastSender;
    //this player uuid
    private String playerUUID;
    //after message for player
    private String afterMessage = "not";
    private String vaultSuffix = "";

    //ignore list from player
    private List<String> ignoreList = new ArrayList<>();
    //themes list from player
    private List<String> themesList;

    //inventory when player use /actions
    private Inventory actionsInventory = null;
    //last clicked inventory
    private Inventory lastClickedInventory = null;

    //hashmap contains all inventory which creates due to /ignorelist
    private HashMap<Integer, Inventory> ignoreListInventories = new HashMap<>();

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
            plugin.getLogger().warning("Error save ignoreFile");
        }
    }

    public void setWorldColor(World world){
        //get boolean
        if(UtilsMain.getConfigBoolean("world-color.enable")){
            //get player name
            String eventWorldName = world.getName();
            //get color
            String worldColor = UtilsMain.getConfigString("world-color." + eventWorldName);
            //set color
            worldColor = worldColor + player.getName();
            worldColor = UtilsMessage.formatString(worldColor);
            worldColor = getVaultPrefix() + worldColor + getVaultSuffix();
            player.setPlayerListName(worldColor);
        }
    }

    public void setThemesList(List<String> themesList){
        this.themesList = themesList;
        plugin.themesFileConfig.set(playerUUID, this.themesList);

        try {
            plugin.themesFileConfig.save(plugin.themesFile);
        } catch (IOException e){
            plugin.getLogger().warning("Error save themesFile");
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

    public void setIgnoreListInventories(HashMap<Integer, Inventory> ignoreListInventories) {
        this.ignoreListInventories = ignoreListInventories;
    }

    public HashMap<Integer, Inventory> getIgnoreListInventories() {
        return ignoreListInventories;
    }

    public String getVaultPrefix() {
        String prefix = "";
        if(FlectoneChat.vaultLoad){
            prefix = plugin.getServer().getServicesManager().getRegistration(Chat.class).getProvider().getPlayerPrefix(player);
        }
        return UtilsMessage.formatString(prefix);
    }

    public String getVaultSuffix() {
        if(FlectoneChat.vaultLoad){
            this.vaultSuffix = plugin.getServer().getServicesManager().getRegistration(Chat.class).getProvider().getPlayerSuffix(player);
        }
        return this.vaultSuffix;
    }

    public void setVaultSuffix(String vaultSuffix) {
        plugin.getServer().getServicesManager().getRegistration(Chat.class).getProvider().setPlayerSuffix(player, vaultSuffix);
        setWorldColor(player.getWorld());
        this.vaultSuffix = vaultSuffix;
    }
}
