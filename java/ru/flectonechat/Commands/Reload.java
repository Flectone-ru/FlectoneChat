package ru.flectonechat.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.Utilities;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "reload.usage");
            return true;
        }
        if(!args[0].equals("reload")){
            Utilities.sendErrorMessage(eventPlayer, "reload.usage");
            return true;
        }
        FlectoneChat plugin = FlectoneChat.getInstance();
        plugin.reloadConfig();
        plugin.loadLanguage();

        String formatMessage = Utilities.getLanguageString("reload.message");
        formatMessage = Utilities.setPlayerColors(formatMessage, eventPlayer.getName());

        eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(formatMessage));

        return true;
    }
}
