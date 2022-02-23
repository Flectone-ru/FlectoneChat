package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.PlayerActions.JoinAndLeft;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsTell;

import java.util.ArrayList;
import java.util.List;

public class Chatcolor implements Listener, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!UtilsMain.senderIsPlayer(sender)) return true;
        //command: /chatcolor (color) (color)
        //supported hex and default minecraft colors
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 2) && !args[0].equals("default")){
            UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.usage");
            return true;
        }

        String themeOne = args[0];

        List<String> themesList = new ArrayList<>();
        //get flectone player
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(sender.getName());

        if(themeOne.equals("default")){
            //create default themes
            themesList = UtilsMain.createDefaultThemes();

            flectonePlayer.setThemesList(themesList);

            UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.success");
            JoinAndLeft.checkTabList(eventPlayer, "footer");
            JoinAndLeft.checkTabList(eventPlayer, "header");
            return true;
        }

        String themeTwo = args[1];
        //check for correct colors in args
        if(checkCorrectThemes(themeOne, eventPlayer)) return true;
        if(checkCorrectThemes(themeTwo, eventPlayer)) return true;

        themesList.add(themeOne);
        themesList.add(themeTwo);
        //save themes
        flectonePlayer.setThemesList(themesList);

        UtilsTell.sendMessageLanguage(eventPlayer, "chatcolor.success");
        JoinAndLeft.checkTabList(eventPlayer, "footer");
        JoinAndLeft.checkTabList(eventPlayer, "header");
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
