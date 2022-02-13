package ru.flectonechat.Tools.Utils;

import org.bukkit.World;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;

import java.util.ArrayList;
import java.util.List;

public class UtilsMain {
    //get string from config file
    public static String getConfigString(String stringName){
        return FlectoneChat.getInstance().getConfig().getString(stringName);
    }
    //get string from language file
    public static String getLanguageString(String stringName){
        return FlectoneChat.getInstance().language.getString(stringName);
    }
    //get boolean from config file
    public static Boolean getConfigBoolean(String stringName){
        return FlectoneChat.getInstance().getConfig().getBoolean(stringName);
    }
    //get int from config file
    public static Integer getConfigInt(String stringName){
        return FlectoneChat.getInstance().getConfig().getInt(stringName);
    }
    //create new flectone player
    public static void createFlectonePlayer(Player player){
        //get plugin
        FlectoneChat plugin = FlectoneChat.getInstance();
        //if flectone player exist
        if(plugin.allPlayers.get(player.getName()) != null) return;
        //create flectone player
        FlectonePlayer flectonePlayer = new FlectonePlayer();
        flectonePlayer.setPlayer(player);
        //get ignore list player
        List<String> ignoreList = plugin.ignoreFileConfig.getStringList(player.getUniqueId().toString());
        //save ignore list
        flectonePlayer.setIgnoreList(ignoreList);
        //set world color
        flectonePlayer.setWorldColor(player.getWorld());
        //get themes list player
        List<String> themesList = plugin.themesFileConfig.getStringList(player.getUniqueId().toString());
        //create themes list
        if(themesList.size() == 0){
            themesList = createDefaultThemes();
        }
        //save themes list
        flectonePlayer.setThemesList(themesList);
        //save flectone player
        plugin.allPlayers.put(player.getName(), flectonePlayer);
    }
    //create default themes for player
    public static List<String> createDefaultThemes(){
        //get theme first
        String themeFirst = UtilsMain.getConfigString("color.main");
        //get theme second
        String themeSecond = UtilsMain.getConfigString("color.text");
        //new list
        List<String> list = new ArrayList<>();
        //add themes
        list.add(themeFirst);
        list.add(themeSecond);
        return list;
    }
    //set world color
    public static void useSetWorldColor(Player player, World world){
        FlectoneChat plugin = FlectoneChat.getInstance();
        plugin.allPlayers.get(player.getName()).setWorldColor(world);
    }
}
