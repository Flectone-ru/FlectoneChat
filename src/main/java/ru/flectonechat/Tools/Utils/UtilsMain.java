package ru.flectonechat.Tools.Utils;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
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
    //get list from language file
    public static List<String> getLanguageList(String stringName){
        return FlectoneChat.getInstance().language.getStringList(stringName);
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

        FlectoneChat plugin = FlectoneChat.getInstance();

        if(plugin.allPlayers.get(player.getName()) != null) return;
        //create flectone player
        FlectonePlayer flectonePlayer = new FlectonePlayer();
        flectonePlayer.setPlayer(player);

        List<String> ignoreList = plugin.ignoreFileConfig.getStringList(player.getUniqueId().toString());

        flectonePlayer.setIgnoreList(ignoreList);

        flectonePlayer.setWorldColor(player.getWorld());

        List<String> themesList = plugin.themesFileConfig.getStringList(player.getUniqueId().toString());
        //create themes list
        if(themesList.size() == 0){
            themesList = createDefaultThemes();
        }

        flectonePlayer.setThemesList(themesList);

        plugin.allPlayers.put(player.getName(), flectonePlayer);
    }
    //create default themes for player
    public static List<String> createDefaultThemes(){

        String themeFirst = UtilsMain.getConfigString("color.main");
        String themeSecond = UtilsMain.getConfigString("color.text");

        List<String> list = new ArrayList<>();

        list.add(themeFirst);
        list.add(themeSecond);
        return list;
    }
    //set world color
    public static void useSetWorldColor(Player player, World world){
        FlectoneChat plugin = FlectoneChat.getInstance();
        plugin.allPlayers.get(player.getName()).setWorldColor(world);
    }
    //if sender - console
    public static boolean senderIsPlayer(CommandSender player){
        boolean isPlayer = player instanceof Player;
        if(!isPlayer) FlectoneChat.getInstance().getLogger().warning("This command not support console");
        return isPlayer;
    }
}
