package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utilities;

public class Me implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "me.usage");
            return true;
        }
        String formatMessage = Utilities.getConfigString("me.format");

        String message = Utilities.createMessageFromArgs(args, 0, "");
        formatMessage = formatMessage.replace("<message>", message).replace("<player>", eventPlayer.getName());

        Utilities.sendEveryoneMessage(formatMessage, eventPlayer);

        return true;
    }
}
