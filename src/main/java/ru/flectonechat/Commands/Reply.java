package ru.flectonechat.Commands;

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
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();

        if(UtilsCommand.checkArgs(args, 1)){
            UtilsTell.sendErrorMessage(eventPlayer, "reply.usage");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        String lastSender = flectonePlayer.getLastSender();

        if(lastSender == null){
            UtilsTell.sendErrorMessage(eventPlayer, "reply.no_player_answer");
            return true;
        }
        Player receiver = UtilsMain.checkPlayerOnServer(lastSender);
        if(receiver == null){
            UtilsTell.sendErrorMessage(eventPlayer, "reply.no_player");
            return true;
        }

        String message = UtilsMessage.createMessageFromArgs(args, 0, "<color_text>");

        if(UtilsTell.useCommandTell(message, eventPlayer, receiver, "sender")){
            return true;
        }

        UtilsTell.useCommandTell(message, receiver, eventPlayer, "receiver");

        return true;
    }
}
