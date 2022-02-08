package ru.flectonechat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();

        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "reply.usage");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        String lastSender = flectonePlayer.getLastSender();

        if(lastSender == null){
            Utilities.sendErrorMessage(eventPlayer, "reply.no_player_answer");
            return true;
        }
        Player receiver = Utilities.checkPlayerOnServer(lastSender);
        if(receiver == null){
            Utilities.sendErrorMessage(eventPlayer, "reply.no_player");
            return true;
        }

        String message = Utilities.createMessageFromArgs(args, 0, "<color_text>");

        if(Utilities.useCommandTell(message, eventPlayer, receiver, "sender")){
            return true;
        }
        if(Utilities.useCommandTell(message, receiver, eventPlayer, "receiver")){
            return true;
        }

        return true;
    }
}
