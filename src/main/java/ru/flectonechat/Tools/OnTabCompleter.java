package ru.flectonechat.Tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.flectonechat.FlectoneChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class OnTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //create new list
        List<String> wordsList = new ArrayList<>();
        //command
        switch(command.getName().replace(" ", "")){
            case "chatcolor": chatcolorTabComplete(args, wordsList); break;
            case "tell": tellTabComplete(args, wordsList, "tell"); break;
            case "reply": tellTabComplete(args, wordsList, "r"); break;
            case "me":
            case "try ": wordsList.add("(message)"); break;
            case "ignore": ignoreTabComplete(wordsList, args); break;
            case "flectonechat": flectonechatArray(wordsList, args); break;
            case "stream": streamTabComplete(args, wordsList); break;
        }
        Collections.sort(wordsList);
        return wordsList;
    }
    //words for /chatcolor
    private void chatcolorTabComplete(String[] args, List<String> wordsList){
        if(args.length == 1){
            wordsList.add("default");
            wordsList.add("#ffffff");
            wordsList.add("&b");
        }
        if(args.length == 2) {
            wordsList.add("#dddddd");
            wordsList.add("&f");
        }
    }
    //words for /stream
    private void streamTabComplete(String[] args, List<String> wordsList){
        if(args.length == 1){
            isStartsWith(args[0],"start", wordsList);
            isStartsWith(args[0],"off", wordsList);
        }
        if(args.length > 1 && args[0].equals("start")){
            wordsList.add("(url)");
        }
    }
    //words for /tell
    private void tellTabComplete(String[] args, List<String> wordsList, String commandName){
        if(commandName.equals("tell") && args.length == 1){
            for(Player player : Bukkit.getOnlinePlayers()){
                wordsList.add(player.getName());
            }
        }
        if(commandName.equals("tell") && args.length == 2 || commandName.equals("r") && args.length == 1) {
            wordsList.add("(message)");
        }
    }
    //words for /ignore
    private void ignoreTabComplete(List<String> wordsList, String[] args){
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
            isStartsWith(args[0], player.getName(), wordsList);
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName().toLowerCase();
            if(!wordsList.contains(playerName)){
                isStartsWith(args[0], player.getName(), wordsList);
            }
        }
    }
    //words for /flectonechat
    private void flectonechatArray(List<String> wordsList, String[] args){
        FlectoneChat plugin = FlectoneChat.getInstance();
        if(args.length == 1){
            isStartsWith(args[0], "reload", wordsList);
            isStartsWith(args[0], "config", wordsList);
            isStartsWith(args[0], "language", wordsList);
        }
        if(args.length == 2){
            if(args[0].equals("config")){
                addKeysFile(plugin.getConfig().getKeys(true), wordsList, args[1]);
            }
            if(args[0].equals("language")){
                addKeysFile(plugin.language.getKeys(true), wordsList, args[1]);
            }
        }
        if(args.length == 3) {
            wordsList.add("set");
        }
        if(args.length == 4){
            wordsList.add("string");
            wordsList.add("integer");
            wordsList.add("boolean");
        }
    }
    //if message starts with arg then add to words
    private void isStartsWith(String arg, String string, List<String> wordsList){
        if(string.toLowerCase().startsWith(arg.toLowerCase())){
            wordsList.add(string);
        }
    }
    //if keys starts with arg then add to words
    private void addKeysFile(Set<String> keys, List<String> wordsList, String arg){
        for(String key : keys){
            isStartsWith(arg, key, wordsList);
        }
    }
}
