package ru.flectonechat.Tools.Utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsMain {

    public static String formatString(String string){

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String hexCode = string.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
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
            themesList = createDefaultThemes();
        }

        flectonePlayer.setThemesList(themesList);

        plugin.allPlayers.put(player.getName(), flectonePlayer);
    }

    public static List<String> createDefaultThemes(){
        String themeOne = UtilsMain.getConfigString("color.main");
        String themeTwo = UtilsMain.getConfigString("color.text");

        List<String> list = new ArrayList<>();
        list.add(themeOne);
        list.add(themeTwo);
        return list;
    }

    public static Player checkPlayerOnServer(String playerName){
        for(Player player : Bukkit.getOnlinePlayers()){
            String name = player.getName();
            if(playerName.equals(name)){
                return Bukkit.getPlayer(playerName);
            }
        }
        return null;
    }

}
