package ru.flectonechat.Tools.Utils;

import org.bukkit.entity.Player;

public class UtilsCommand {
    //checking for args
    public static boolean checkArgs(String[] args, int minArgs){
        return args.length < minArgs;
    }
    public static boolean hasPermission(Player player, String string){
        return player.hasPermission(string);
    }
}
