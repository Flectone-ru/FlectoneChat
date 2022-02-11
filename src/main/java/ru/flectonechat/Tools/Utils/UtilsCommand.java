package ru.flectonechat.Tools.Utils;

public class UtilsCommand {
    public static boolean checkArgs(String[] args, int minArgs){
        return args.length < minArgs;
    }
}
