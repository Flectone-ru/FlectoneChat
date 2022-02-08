package ru.flectonechat.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.flectonechat.FlectoneChat;
import ru.flectonechat.PlayerActions.GUI;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Ignore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        String eventPlayerName = eventPlayer.getName();
        if(Utilities.checkArgsCommand(args, 1)){
            Utilities.sendErrorMessage(eventPlayer, "ignore.usage");
            return true;
        }

        OfflinePlayer mutPlayer = Bukkit.getOfflinePlayer(args[0]);
        if(mutPlayer.getName() == eventPlayerName){
            Utilities.sendErrorMessage(eventPlayer, "ignore.myself");
            return true;
        }

        if(!mutPlayer.hasPlayedBefore() || !Bukkit.getOnlinePlayers().contains(mutPlayer)){
            Utilities.sendErrorMessage(eventPlayer, "ignore.no_player");
            return true;
        }

        FlectoneChat plugin = FlectoneChat.getInstance();
        FlectonePlayer flectonePlayer = plugin.allPlayers.get(eventPlayerName);
        List<String> ignoreList = flectonePlayer.getIgnoreList();
        if(ignoreList.size() == 0){
            ignoreList = new ArrayList<>();
        }

        String mutPlayerUUID = mutPlayer.getUniqueId().toString();

        if(ignoreList.contains(mutPlayerUUID)){
            ignoreList.remove(mutPlayerUUID);
            flectonePlayer.setIgnoreList(ignoreList);

            Utilities.setIgnoreListInventory(flectonePlayer, flectonePlayer.getIgnoreListInventorys(), true);

            sendMessage("un_mute", eventPlayer, args[0]);
            return true;
        }

        ignoreList.add(mutPlayerUUID);
        flectonePlayer.setIgnoreList(ignoreList);

        sendMessage("mute", eventPlayer, args[0]);

        return true;
    }

    private void sendMessage(String format, Player eventPlayer, String mutPlayerName){
        String formatMessage = Utilities.getLanguageString("ignore." + format);
        formatMessage = Utilities.formatString(formatMessage).replace("<player>", mutPlayerName);

        eventPlayer.spigot().sendMessage(TextComponent.fromLegacyText(formatMessage));
    }
}
