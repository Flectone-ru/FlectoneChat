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

import java.util.List;

public class Stream implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player eventPlayer = (Player) sender;
        //checking for args
        if(UtilsCommand.checkArgs(args, 1) || (args.length == 1 && args[0].equals("start"))){
            UtilsTell.sendMessageLanguage(eventPlayer, "stream.usage");
            return true;
        }
        //check permission
        if(!UtilsCommand.hasPermission(eventPlayer, "flectonechat.stream")){
            UtilsTell.sendMessageLanguage(eventPlayer, "stream.permission");
            return true;
        }
        //check vault plugin on server
        if(!FlectoneChat.vaultLoad){
            UtilsTell.sendMessageLanguage(eventPlayer, "stream.vault-error");
            return true;
        }
        //get flectone player
        FlectonePlayer flectonePlayer = FlectoneChat.getInstance().allPlayers.get(eventPlayer.getName());
        switch(args[0]){
            case "off":
                //if player use command and have not stream
                if(!checkPlayerSuffix(flectonePlayer.getVaultSuffix())){
                    UtilsTell.sendMessageLanguage(eventPlayer, "stream.not-start");
                    return true;
                }
                //clear suffix and send message
                flectonePlayer.setVaultSuffix("");
                UtilsTell.sendMessageLanguage(eventPlayer, "stream.offed");
                return true;
            case "start":
                //if player use command and have stream
                if(checkPlayerSuffix(flectonePlayer.getVaultSuffix())){
                    UtilsTell.sendMessageLanguage(eventPlayer, "stream.not-off");
                    return true;
                }
                //set suffix
                String streamSuffix = UtilsMain.getLanguageString("stream.suffix");
                flectonePlayer.setVaultSuffix(UtilsMessage.formatString(streamSuffix));
                //create list and send
                List<String> formatList = UtilsMain.getLanguageList("stream.start");
                addArgsToList(args, 1, formatList);
                sendMessage(formatList, eventPlayer.getName());
                return true;
        }
        return true;
    }
    //check player suffix
    private boolean checkPlayerSuffix(String playerSuffix){
        if(playerSuffix == null || playerSuffix.equals("")){
            return false;
        }
        return true;
    }
    //send finished message
    private void sendMessage(List<String> list, String eventPlayerName){
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            String formatList = createFormatString(list, onlinePlayer, eventPlayerName);
            UtilsTell.sendMessage(onlinePlayer, formatList);
        }
    }
    //create string from list
    private String createFormatString(List<String> list, Player player, String eventPlayerName){
        String formatList = "";
        for(String string : list){
            string = string.replace("<player>", eventPlayerName);
            string = UtilsMessage.setPlayerColors(string, player.getName());
            formatList = formatList + "\n" + string;
        }
        return formatList;
    }
    //add string args to list
    private void addArgsToList(String[] args, int firstArg, List<String> formatList){
        for(int x = firstArg; x < args.length; x++){
            String string = "<color_text>" + args[x];
            formatList.add(string);
        }
    }
}
