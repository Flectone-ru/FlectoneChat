package ru.flectonechat.Tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class OnTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> array = new ArrayList<>();
        if(command.getName().equals("chatcolor")){
            chatcolorTabComplete(args, array);
        }
        if(command.getName().equals("tell")){
            tellTabComplete(args, array, "tell");
        }
        if(command.getName().equals("reply")){
            tellTabComplete(args, array, "r");
        }
        if(command.getName().equals("try") || command.getName().equals("me")){
            array.add("(message)");
        }
        if(command.getName().equals("flectonechat")){
            array.add("reload");
        }
        if(command.getName().equals("ignore")){
            ignoreTabComplete(array, args);
            Collections.sort(array);

            return array;
        }

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
            String playerName = player.getName().toLowerCase();
            if(playerName.startsWith(args[0].toLowerCase())){
                array.add(player.getName());
            }
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName().toLowerCase();
            if(!array.contains(playerName)){
                if(playerName.startsWith(args[0].toLowerCase())){
                    array.add(player.getName());
                }
            }
        }
    }
}
