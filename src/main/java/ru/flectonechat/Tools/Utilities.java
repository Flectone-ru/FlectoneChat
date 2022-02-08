package ru.flectonechat.Tools;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.flectonechat.FlectoneChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static String formatString(String string){

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String hexCode = string.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            string = string.replace(hexCode, builder.toString());
            matcher = pattern.matcher(string);
        }

        string = ChatColor.translateAlternateColorCodes('&', string);

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getConfigString(String stringName){
        return FlectoneChat.getInstance().getConfig().getString(stringName);
    }

    public static String getLanguageString(String stringName){
        return FlectoneChat.getInstance().language.getString(stringName);
    }

    public static Boolean getConfigBoolean(String stringName){
        return FlectoneChat.getInstance().getConfig().getBoolean(stringName);
    }

    public static Integer getConfigInt(String stringName){
        return FlectoneChat.getInstance().getConfig().getInt(stringName);
    }

    public static void setClickEvent(String command, String text, TextComponent textComponent, Player eventPlayer, Player player){
        boolean mySelfMessageEnable = Utilities.getConfigBoolean("myself_message.enable");

        text = Utilities.formatString(text);

        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(text))));

        if(!mySelfMessageEnable && eventPlayer == player){
            textComponent = new TextComponent(TextComponent.fromLegacyText(text));
        }
    }

    public static String replaceMessagePlayer(String string, String replaceWith){
        string = string.replace("<player>", replaceWith);
        return string;
    }

    public static String setPlayerColors(String string, String playerName){
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(playerName);

        List<String> themesList = flectonePlayer.getThemesList();

        string = string.replace("<color_main>", themesList.get(0));
        string = string.replace("<color_text>", themesList.get(1));

        string = Utilities.formatString(string);

        return string;
    }

    public static boolean checkArgsCommand(String[] args, int minArgs){
        if(args.length < minArgs){
            return true;
        }
        return false;
    }

    public static void sendErrorMessage(Player player, String stringLanguage){
        String string = Utilities.getLanguageString(stringLanguage);
        string = Utilities.setPlayerColors(string, player.getName());
        string  = Utilities.formatString(string );
        player.spigot().sendMessage(TextComponent.fromLegacyText(string ));
    }

    public static void createFlectonePlayer(Player player){
        FlectoneChat plugin = FlectoneChat.getInstance();

        if(plugin.allPlayers.get(player.getName()) != null) return;

        FlectonePlayer flectonePlayer = new FlectonePlayer();
        flectonePlayer.setPlayer(player);

        List<String> ignoreList = plugin.ignoreFileConfig.getStringList(player.getUniqueId().toString());
        flectonePlayer.setIgnoreList(ignoreList);

        flectonePlayer.setWorldColor(player.getWorld());

        List<String> themesList = plugin.themesFileConfig.getStringList(player.getUniqueId().toString());

        if(themesList.size() == 0){
            themesList = createDefaultThemes(themesList);
        }

        flectonePlayer.setThemesList(themesList);

        plugin.allPlayers.put(player.getName(), flectonePlayer);
    }

    public static List<String> createDefaultThemes(List<String> list){
        String themeOne = Utilities.getConfigString("color.main");
        String themeTwo = Utilities.getConfigString("color.text");

        list = new ArrayList<>();
        list.add(themeOne);
        list.add(themeTwo);
        return list;
    }

    public static boolean useCommandTell(String message, Player eventPlayer, Player receiver, String whomMessage){
        FlectoneChat plugin = FlectoneChat.getInstance();
        String eventPlayerName = eventPlayer.getName();
        String receiverName = receiver.getName();

        FlectonePlayer flectonePlayer = plugin.allPlayers.get(receiverName);
        if(flectonePlayer.getIgnoreList().contains(eventPlayer.getUniqueId().toString())){
            Utilities.sendErrorMessage(eventPlayer, "tell.you_ignored");
            return true;
        }

        FlectonePlayer flectonePlayer1 = plugin.allPlayers.get(eventPlayerName);
        if(flectonePlayer1.getIgnoreList().contains(receiver.getUniqueId().toString())){
            Utilities.sendErrorMessage(eventPlayer, "tell.you_ignore");
            return true;
        }


        String formatString = Utilities.getLanguageString("tell." + whomMessage + ".message");
        formatString = Utilities.setPlayerColors(formatString, eventPlayerName).replace("<player>", receiverName);

        String clickMessage = Utilities.getLanguageString("click.message");
        clickMessage = Utilities.setPlayerColors(clickMessage, eventPlayerName).replace("<player>", receiverName);

        TextComponent formatComponent = new TextComponent(TextComponent.fromLegacyText(formatString));

        Utilities.setClickEvent("/actions " + receiverName, clickMessage,formatComponent, eventPlayer, receiver);

        ComponentBuilder formatBuilder = new ComponentBuilder();
        formatBuilder.append(formatComponent);

        message = Utilities.setPlayerColors(message, eventPlayerName);
        formatBuilder.append(TextComponent.fromLegacyText(message), ComponentBuilder.FormatRetention.NONE);

        eventPlayer.spigot().sendMessage(formatBuilder.create());

        plugin.allPlayers.get(receiverName).setLastSender(eventPlayerName);
        return false;
    }

    public static Player checkPlayerOnServer(String playerName){
        for(Player player : Bukkit.getOnlinePlayers()){
            String name = player.getName();
            if(playerName.equals(name)){
                return (Player) Bukkit.getOfflinePlayer(playerName);
            }
        }
        return null;
    }

    public static String createMessageFromArgs(String[] args, int firstArg, String color){
        String message = color;
        for(int x = firstArg; x < args.length; x++){
            message = message + args[x] + color + " ";
        }
        return message;
    }

    public static TextComponent searchPingMessage(String message, Player eventPlayer, Player player){

        TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(message));

        String playerName = player.getName();
        String playerPing = checkPingPlayer(message);

        if(!playerPing.isEmpty()){
            String pingColor = Utilities.getConfigString("player_ping.color");
            pingColor = Utilities.setPlayerColors(pingColor, playerName);

            message = pingColor + ChatColor.stripColor(message);

            messageComponent = new TextComponent(TextComponent.fromLegacyText(message));

            String clickMessage = Utilities.getLanguageString("click.message").replace("<player>", playerPing);
            clickMessage = Utilities.setPlayerColors(clickMessage, playerName);

            Utilities.setClickEvent("/actions " + playerPing, clickMessage, messageComponent, eventPlayer, player);
        }

        return messageComponent;
    }

    public static String checkPingPlayer(String message){
        String pingCondition = Utilities.getConfigString("player_ping.condition");
        boolean playerPingEnable = Utilities.getConfigBoolean("player_ping.enable");

        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            if(message.contains(playerName) && message.contains(pingCondition) && playerPingEnable){
                return playerName;
            }
        }
        return "";
    }

    public static void sendEveryoneMessage(String message, Player eventPlayer){
        FlectoneChat plugin = FlectoneChat.getInstance();
        for(Player player : Bukkit.getOnlinePlayers()){
            List<String> ignoreList = plugin.allPlayers.get(player.getName()).getIgnoreList();
            if(!ignoreList.contains(eventPlayer.getUniqueId().toString())){
                String formatMessage = Utilities.setPlayerColors(message, player.getName());
                player.spigot().sendMessage(TextComponent.fromLegacyText(formatMessage));
            }
        }
    }

    public static ItemStack createHeadInventory(String headName, String playerName){
        ItemStack blockForHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) blockForHead.getItemMeta();
        skullMeta.setDisplayName(Utilities.formatString("&r" + headName));
        skullMeta.setOwner(playerName);
        blockForHead.setItemMeta(skullMeta);
        return blockForHead;
    }

    public static ItemStack createArrowInventory(ItemStack arrow, String arrowName){
        ItemMeta itemMeta = arrow.getItemMeta();
        arrowName = Utilities.formatString(arrowName);
        itemMeta.setDisplayName(arrowName);
        arrow.setItemMeta(itemMeta);
        return arrow;
    }

    public static void setIgnoreListInventory(FlectonePlayer flectonePlayer, HashMap ignoreListInventorys, boolean createNew){
        String inventoryName = Utilities.getLanguageString("ignorelist.inventory.name");
        inventoryName = Utilities.formatString(inventoryName);

        List<String> ignoreList = flectonePlayer.getIgnoreList();

        for(int x = 1; x < ignoreList.size(); x++){

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
            ItemStack playerHead = Utilities.createHeadInventory(ignoredPlayer.getName(), ignoredPlayer.getName());

            int numberItem = x;
            int inventoryPage = x/9;
            if(inventoryPage > 0){
                numberItem = x - 9*inventoryPage;
            }

            Inventory inventory = (Inventory) ignoreListInventorys.get(inventoryPage);

            inventory.setItem(numberItem, playerHead);

            if(inventoryPage != 0){
                String backArrowName = Utilities.getLanguageString("ignorelist.inventory.back_arrow");
                ItemStack backArrow = new ItemStack(Material.ARROW);
                backArrow = Utilities.createArrowInventory(backArrow, backArrowName);
                inventory.setItem(9, backArrow);
            }

            if(inventoryPage != (ignoreList.size()-1)/9){
                String nextArrowName = Utilities.getLanguageString("ignorelist.inventory.next_arrow");
                ItemStack nextArrow = new ItemStack(Material.SPECTRAL_ARROW);
                nextArrow = Utilities.createArrowInventory(nextArrow, nextArrowName);
                inventory.setItem(17, nextArrow);
            }
        }
    }
}
