package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "reload.usage");
            return true;
        }
        if(!args[0].equals("reload")){
            UtilsTell.sendErrorMessage(eventPlayer, "reload.usage");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        plugin.reloadConfig();
        plugin.loadLanguage();

        String formatMessage = UtilsMessage.defaultLanguageString("reload.message", eventPlayer.getName());
        UtilsTell.spigotMessage(eventPlayer, formatMessage);

        return true;
    }
}
