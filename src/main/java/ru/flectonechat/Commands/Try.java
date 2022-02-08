package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utilities;

import java.util.Random;

public class Try implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "try.usage");
            return true;
        }

        Random random = new Random();
        int intRandom = random.nextInt(101);
        String intRandomString = String.valueOf(intRandom);

        String getTryResult = String.valueOf(checkTry(intRandom));
        String formatMessage = Utilities.getConfigString("try." + getTryResult + ".format");
        String message = Utilities.createMessageFromArgs(args, 0, "");

        formatMessage = formatMessage
                .replace("<message>", message)
                .replace("<player>", eventPlayer.getName())
                .replace("<chance>", intRandomString);

        Utilities.sendEveryoneMessage(formatMessage, eventPlayer);


        return true;
    }

    private boolean checkTry(Integer integer){
        if(integer < 50){
            return false;
        }
        return true;
    }
}
