package ru.flectonechat.Tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.flectonechat.FlectoneChat;

import java.util.*;

public class OnTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //create new list
        List<String> array = new ArrayList<>();
        //command /chatcolor
        if(command.getName().equals("chatcolor")) chatcolorTabComplete(args, array);
        //command /tell
        if(command.getName().equals("tell")) tellTabComplete(args, array, "tell");
        //command /reply
        if(command.getName().equals("reply")) tellTabComplete(args, array, "r");
        //command /try
        if(command.getName().equals("try") || command.getName().equals("me")) array.add("(message)");
        //command /flectonechat
        if(command.getName().equals("flectonechat")) flectonechatArray(array, args);
        //command /ignore
        if(command.getName().equals("ignore")) ignoreTabComplete(array, args);
        //sort list
        Collections.sort(array);
        return array;
    }

    private void chatcolorTabComplete(String[] args, List<String> array){
        if(args.length == 1){
            array.add("default");
            array.add("#ffffff");
            array.add("&b");
        }
        if(args.length == 2){
            array.add("#dddddd");
            array.add("&f");
        }
    }

    private void tellTabComplete(String[] args, List<String> array, String commandName){
        if(commandName.equals("tell") && args.length == 1){
            for(Player player : Bukkit.getOnlinePlayers()){
                array.add(player.getName());
            }
        }
        if(commandName.equals("tell") && args.length == 2 || commandName.equals("r") && args.length == 1){
            array.add("(message)");
        }
    }

    private void ignoreTabComplete(List<String> array, String[] args){
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
            isStartsWith(args[0], player.getName(), array);
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName().toLowerCase();
            if(!array.contains(playerName)){
                isStartsWith(args[0], player.getName(), array);
            }
        }
    }
    private void flectonechatArray(List<String> array, String[] args){
        FlectoneChat plugin = FlectoneChat.getInstance();
        if(args.length == 1){
            isStartsWith(args[0], "reload", array);
            isStartsWith(args[0], "config", array);
            isStartsWith(args[0], "language", array);
        }
        if(args.length == 2){
            if(args[0].equals("config")){
                addKeysFile(plugin.getConfig().getKeys(true), array, args[1]);
            }
            if(args[0].equals("language")){
                addKeysFile(plugin.language.getKeys(true), array, args[1]);
            }
        }
        if(args.length == 3){
            array.add("set");
        }
        if(args.length == 4 && args[3].equals("")){
            array.add("int");
            array.add("string");
            array.add("boolean");
        }

    }
    //if message starts with arg when add to array
    private void isStartsWith(String arg, String string, List<String> array){
        if(string.toLowerCase().startsWith(arg.toLowerCase())){
            array.add(string);
        }
    }
    //if keys starts with arg then add to array
    private void addKeysFile(Set<String> keys, List<String> array, String arg){
        for(String key : keys){
            isStartsWith(arg, key, array);
        }
    }
}
