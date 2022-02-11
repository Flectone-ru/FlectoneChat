package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

public class Tell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(UtilsCommand.checkArgs(args, 2)){
            UtilsTell.sendErrorMessage(eventPlayer, "tell.usage");
            return true;
        }

        Player receiver = UtilsMain.checkPlayerOnServer(args[0]);
        if(receiver == null){
            UtilsTell.sendErrorMessage(eventPlayer, "tell.no_player");
            return true;
        }

        String message = UtilsMessage.createMessageFromArgs(args, 1, "<color_text>");

        if(receiver == eventPlayer){
            UtilsTell.identicalPlayer(eventPlayer, message);
            return true;
        }

        if(UtilsTell.useCommandTell(message, eventPlayer, receiver, "sender")){
            return true;
        }

        UtilsTell.useCommandTell(message, receiver, eventPlayer, "receiver");

        return true;
    }
}
