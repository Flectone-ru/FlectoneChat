package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

public class Flectonechat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //command: /flectonechat reload or config/language (key) set (value)
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 1) || !args[0].equals("reload") && UtilsCommand.checkArgs(args, 4)){
            UtilsTell.sendMessageLanguage(eventPlayer, "flectonechat.usage");
            return true;
        }
        FlectoneChat plugin = FlectoneChat.getInstance();
        //if command == reload then skip
        if(!args[0].equals("reload")){
            //check "set"
            if(!args[2].equals("set")){
                UtilsTell.sendMessageLanguage(eventPlayer, "flectonechat.usage");
                return true;
            }
            //if (key) doesn't exist
            if(isExistsConfig(args[1]) && isExistsLanguage(args[1])){
                UtilsTell.sendMessageLanguage(eventPlayer, "flectonechat.not-exists");
                return true;
            }
            //message for set
            String message = UtilsMessage.createMessageFromArgs(args, 3, "");
            //set and save config.yml
            if(args[0].equals("config")){
                plugin.getConfig().set(args[1], message);
                plugin.saveConfig();
            }
            //set and save "language".yml
            if(args[0].equals("language")){
                plugin.language.set(args[1], message);
                plugin.saveLanguage();
            }
        }
        //reload plugin
        plugin.reloadConfig();
        plugin.loadLanguage();
        //send success message
        UtilsTell.sendMessageLanguage(eventPlayer, "flectonechat.message");
        return true;
    }
    //doesn't exist (key) in "language".yml
    private boolean isExistsLanguage(String arg){
        FlectoneChat plugin = FlectoneChat.getInstance();
        for(String key : plugin.language.getKeys(true)){
            if(key.equals(arg)) return false;
        }
        return true;
    }
    //doesn't exist (key) in config.yml
    private boolean isExistsConfig(String arg){
        FlectoneChat plugin = FlectoneChat.getInstance();
        for(String key : plugin.getConfig().getKeys(true)){
            if(key.equals(arg)) return false;
        }
        return true;
    }
}
