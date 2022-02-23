package ru.flectonechat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utils.UtilsCommand;
import ru.flectonechat.Tools.Utils.UtilsMain;
import ru.flectonechat.Tools.Utils.UtilsMessage;
import ru.flectonechat.Tools.Utils.UtilsTell;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!UtilsMain.senderIsPlayer(sender)) return true;
        //command: /r (message)
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        //checking for args
        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendMessageLanguage(eventPlayer, "reply.usage");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        String lastSenderName = flectonePlayer.getLastSender();
        //if event player does not have last sender
        if(lastSenderName == null){
            UtilsTell.sendMessageLanguage(eventPlayer, "reply.no-player-answer");
            return true;
        }

        Player lastSender = Bukkit.getPlayer(lastSenderName);

        if(lastSender == null){
            UtilsTell.sendMessageLanguage(eventPlayer, "reply.no-player");
            return true;
        }
        //create message from args and add color for word
        String message = UtilsMessage.createMessageFromArgs(args, 0, "<color_text>");
        //send message for event player
        if(UtilsTell.useCommandTell(message, eventPlayer, lastSender, "sender")){
            return true;
        }
        //send message for last sender
        UtilsTell.useCommandTell(message, lastSender, eventPlayer, "receiver");
        return true;
    }
}
