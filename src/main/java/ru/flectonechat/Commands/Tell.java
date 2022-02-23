package ru.flectonechat.Commands;

import org.bukkit.Bukkit;
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
        if(!UtilsMain.senderIsPlayer(sender)) return true;
        //command: /tell (player) (message)
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 2)){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.usage");
            return true;
        }

        Player receiver = Bukkit.getPlayer(args[0]);

        if(receiver == null){
            UtilsTell.sendMessageLanguage(eventPlayer, "tell.no-player");
            return true;
        }
        //create message from args and add color for word
        String message = UtilsMessage.createMessageFromArgs(args, 1, "<color_text>");

        if(receiver == eventPlayer){
            UtilsTell.identicalPlayer(eventPlayer, message);
            return true;
        }
        //send message for event player
        if(UtilsTell.useCommandTell(message, eventPlayer, receiver, "sender")) return true;
        //send message for receiver
        UtilsTell.useCommandTell(message, receiver, eventPlayer, "receiver");
        return true;
    }
}
