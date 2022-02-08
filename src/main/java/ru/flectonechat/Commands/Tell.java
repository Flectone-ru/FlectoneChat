package ru.flectonechat.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.Tools.Utilities;

public class Tell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(Utilities.checkArgsCommand(args, 2)){
            Utilities.sendErrorMessage(eventPlayer, "tell.usage");
            return true;
        }

        Player receiver = Utilities.checkPlayerOnServer(args[0]);
        if(receiver == null){
            String message = Utilities.getLanguageString("tell.no_player");
            message = Utilities.formatString(message);
            eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(message));
            return true;
        }

        String message = Utilities.createMessageFromArgs(args, 1, "<color_text>");

        if(Utilities.useCommandTell(message, eventPlayer, receiver, "sender")){
            return true;
        }

        if(Utilities.useCommandTell(message, receiver, eventPlayer, "receiver")){
            return true;
        }

        return true;
    }
}
