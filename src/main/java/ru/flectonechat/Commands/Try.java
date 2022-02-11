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
        Player eventPlayer = (Player) sender;
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "try.usage");
            return true;
        }

        Random random = new Random();
        int intRandom = random.nextInt(101);
        String intRandomString = String.valueOf(intRandom);

        String getTryResult = String.valueOf(checkTry(intRandom));
        String formatMessage = UtilsMain.getConfigString("try." + getTryResult + ".format");
        formatMessage = UtilsMain.formatString(formatMessage);
        formatMessage = UtilsMessage.replacePlayerName(formatMessage, eventPlayer.getName());

        String message = UtilsMessage.createMessageFromArgs(args, 0, "");
        formatMessage = formatMessage.replace("<message>", message).replace("<chance>", intRandomString);

        UtilsTell.sendEveryoneMessage(formatMessage, eventPlayer);
        return true;
    }

    private boolean checkTry(Integer integer){
        if(integer < 50){
            return false;
        }
        return true;
    }
}
