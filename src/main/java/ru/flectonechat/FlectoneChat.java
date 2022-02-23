package ru.flectonechat;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.flectonechat.Commands.*;
import ru.flectonechat.PlayerActions.GUI;
import ru.flectonechat.PlayerActions.JoinAndLeft;
import ru.flectonechat.PlayerActions.ServerList;
import ru.flectonechat.PlayerActions.WorldChange;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.OnTabCompleter;
import ru.flectonechat.Tools.Utils.UtilsMain;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FlectoneChat extends JavaPlugin {

    public HashMap<String, FlectonePlayer> allPlayers = new HashMap<>();

    private static FlectoneChat instance;

    public FileConfiguration language;
    public File ignoreFile;
    public FileConfiguration ignoreFileConfig;

    public File themesFile;
    public FileConfiguration themesFileConfig;

    private static Chat chat = null;

    public static boolean vaultLoad;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 14445);
        //get plugin
        FlectoneChat.instance = this;
        //create config.yml
        createFile("config");
        //create "language".yml
        loadLanguage();
        //create "ignore".yml
        ignoreFile = createFile("ignore");
        ignoreFileConfig = YamlConfiguration.loadConfiguration(ignoreFile);
        //create "themes".yml
        themesFile = createFile("themes");
        themesFileConfig = YamlConfiguration.loadConfiguration(themesFile);
        //register events
        Bukkit.getPluginManager().registerEvents(new WorldChange(), this);
        Bukkit.getPluginManager().registerEvents(new JoinAndLeft(), this);
        Bukkit.getPluginManager().registerEvents(new ChatSettings(), this);
        Bukkit.getPluginManager().registerEvents(new Chatcolor(), this);
        Bukkit.getPluginManager().registerEvents(new GUI(), this);
        Bukkit.getPluginManager().registerEvents(new ServerList(), this);
        //get commands
        getCommand("chatcolor").setExecutor(new Chatcolor());
        getCommand("tell").setExecutor(new Tell());
        getCommand("reply").setExecutor(new Reply());
        getCommand("me").setExecutor(new Me());
        getCommand("try").setExecutor(new Try());
        getCommand("flectonechat").setExecutor(new Flectonechat());
        getCommand("actions").setExecutor(new Actions());
        getCommand("ignore").setExecutor(new Ignore());
        getCommand("ignorelist").setExecutor(new IgnoreList());
        getCommand("stream").setExecutor(new Stream());
        //set tab completer
        getCommand("chatcolor").setTabCompleter(new OnTabCompleter());
        getCommand("tell").setTabCompleter(new OnTabCompleter());
        getCommand("reply").setTabCompleter(new OnTabCompleter());
        getCommand("me").setTabCompleter(new OnTabCompleter());
        getCommand("try").setTabCompleter(new OnTabCompleter());
        getCommand("flectonechat").setTabCompleter(new OnTabCompleter());
        getCommand("ignore").setTabCompleter(new OnTabCompleter());
        getCommand("stream").setTabCompleter(new OnTabCompleter());
        //create flectone player for everyone
        createFlectonePlayers();
        //check vault
        setVaultLoad();
        //load vault
        if(vaultLoad){
            setupChat();
        } else {
            getLogger().warning("Doesn't have plugin Vault");
        }
        //start get logger
        getLogger().info("Start");
    }

    @Override
    public void onDisable() {
        //end get logger
        getLogger().info("Stop");
    }
    //check file exist
    public void checkFile(File file){
        if(!file.exists()){

            if(file.getName().contains("config")){
                getConfig().options().copyDefaults();
                saveDefaultConfig();
                return;
            }
            saveResource(file.getName(), false);
        }
    }
    //get plugin
    public static FlectoneChat getInstance() {
        return instance;
    }
    //create "language".yml
    public void loadLanguage(){

        List<String> defaultLangFiles = Arrays.asList("en_US", "ru_RU");
        //create files for langs
        for(String lang : defaultLangFiles){
            createFile(lang);
        }

        String getLanguageName = getConfig().getString("language");
        //load language
        try {
            //load custom
            language = YamlConfiguration.loadConfiguration(createFile(getLanguageName));
            getLogger().info("Loaded " + getLanguageName + " language");

        } catch (Exception event){
            //load en_US
            language = YamlConfiguration.loadConfiguration(createFile("en_US"));
            getLogger().warning("Failed to load " + getLanguageName + " language");
            getLogger().warning("Loaded default english language...");
        }
    }
    //create file from his name
    public File createFile(String fileName){
        File file = new File(getDataFolder() + File.separator + fileName + ".yml");
        checkFile(file);
        return file;
    }
    //create flectone player for everyone
    public void createFlectonePlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            UtilsMain.createFlectonePlayer(player);
        }
    }
    //save "language".yml
    public void saveLanguage(){

        String languageName = getConfig().getString("language");
        File languageFile = createFile(languageName);

        saveResource(languageName + ".yml", true);

        try {
            language.save(languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //setup vault chat
    private boolean setupChat() {

        chat = getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        return chat != null;
    }

    public static void setVaultLoad() {
        FlectoneChat.vaultLoad = Bukkit.getPluginManager().getPlugin("Vault") != null;
    }
}
