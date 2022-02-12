package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsTell;

import java.util.ArrayList;
import java.util.List;

public class Chatcolor implements Listener, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //command: /chatcolor (color) (color)
        //supported hex and default minecraft colors
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 2) && !args[0].equals("default")){
            UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.usage");
            return true;
        }
        //get arg one
        String themeOne = args[0];
        //create new list
        List<String> themesList = new ArrayList<>();
        //get flectone player
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(sender.getName());
        //check arg one for "default"
        if(themeOne.equals("default")){
            //create default themes
            themesList = UtilsMain.createDefaultThemes();
            //set themes list
            flectonePlayer.setThemesList(themesList);
            //send message
            UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.success");
            return true;
        }
        //get arg two
        String themeTwo = args[1];
        //check for correct colors in args
        if(checkCorrectThemes(themeOne, eventPlayer)) return true;
        if(checkCorrectThemes(themeTwo, eventPlayer)) return true;
        //add themes in list
        themesList.add(themeOne);
        themesList.add(themeTwo);
        //save themes
        flectonePlayer.setThemesList(themesList);
        //send message
        UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.success");
        return true;
    }
    //check for correct color in arg
    private boolean checkCorrectThemes(String theme, Player player){
        if(!theme.contains("#") && !theme.contains("&")){
            UtilsTell.sendMessageLanguage(player, "chatcolor.usage");
            return true;
        }
        return false;
    }
}
