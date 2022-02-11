package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

public class Me implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "me.usage");
            return true;
        }
        String formatMessage = UtilsMain.getConfigString("me.format");
        formatMessage = UtilsMain.formatString(formatMessage);
        formatMessage = UtilsMessage.replacePlayerName(formatMessage, eventPlayer.getName());
        String message = UtilsMessage.createMessageFromArgs(args, 0, "");

        formatMessage = formatMessage.replace("<message>", message);

        UtilsTell.sendEveryoneMessage(formatMessage, eventPlayer);

        return true;
    }
}
