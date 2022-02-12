package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

import java.util.Random;

public class Try implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //command: /try (message)
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendMessageLanguage(eventPlayer, "try.usage");
            return true;
        }
        //create random
        Random random = new Random();
        int intRandom = random.nextInt(101);
        String intRandomString = String.valueOf(intRandom);
        //get result from random
        String getTryResult = String.valueOf(checkTry(intRandom));
        //get format
        String formatMessage = UtilsMain.getConfigString("try." + getTryResult + ".format");
        formatMessage = UtilsMessage.formatString(formatMessage);
        //replace placeholder <player>
        formatMessage = UtilsMessage.replacePlayerName(formatMessage, eventPlayer.getName());
        //create message from args and add color for word
        String message = UtilsMessage.createMessageFromArgs(args, 0, "");
        //replace placeholder <message>
        formatMessage = formatMessage.replace("<message>", message).replace("<chance>", intRandomString);
        //send message
        UtilsTell.sendEveryoneMessage(formatMessage, eventPlayer);
        return true;
    }
    //get result
    private boolean checkTry(Integer integer){
        return integer >= 50;
    }
}
