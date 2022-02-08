package ru.flectonechat.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Chatcolor implements Listener, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "chatcolor.usage");
            return true;
        }

        String themeOne = args[0];
        List<String> themesList = new ArrayList<>();
        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(sender.getName());

        if(themeOne.equals("default")){
            themesList = Utilities.createDefaultThemes(themesList);
            flectonePlayer.setThemesList(themesList);
            sendSuccesMessage(eventPlayer);
            return true;
        }

        if(Utilities.checkArgsCommand(args, 2)){
            Utilities.sendErrorMessage(eventPlayer, "chatcolor.usage");
            return true;
        }

        String themeTwo = args[1];

        if(checkCorrectThemes(themeOne, eventPlayer)) return true;
        if(checkCorrectThemes(themeTwo, eventPlayer)) return true;

        themesList.add(themeOne);
        themesList.add(themeTwo);

        flectonePlayer.setThemesList(themesList);

        sendSuccesMessage(eventPlayer);

        return true;
    }

    private boolean checkCorrectThemes(String theme, Player player){
        if(!theme.contains("#") && !theme.contains("&")){
            Utilities.sendErrorMessage(player, "chatcolor.usage");
            return true;
        }
        return false;
    }

    private void sendSuccesMessage(Player player){
        String succesMessage = Utilities.getLanguageString("chatcolor.succes");
        succesMessage = Utilities.setPlayerColors(succesMessage, player.getName());

        player.spigot().sendMessage(TextComponent.fromLegacyText(succesMessage));
    }
}