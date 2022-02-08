package ru.flectonechat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.flectonechat.Commands.*;
import ru.flectonechat.PlayerActions.GUI;
import ru.flectonechat.Tools.OnTabCompleter;

import ru.flectonechat.PlayerActions.WorldChange;
import ru.flectonechat.PlayerActions.JoinAndLeft;
import ru.flectonechat.Tools.FlectonePlayer;
import ru.flectonechat.Tools.Utilities;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class FlectoneChat extends JavaPlugin {

    public HashMap<String, FlectonePlayer> allPlayers = new HashMap<>();

    private static FlectoneChat instance;

    public FileConfiguration language;
    public File ignoreFile;
    public FileConfiguration ignoreFileConfig;

    public File themesFile;
    public FileConfiguration themesFileConfig;



    @Override
    public void onEnable() {
        FlectoneChat.instance = this;

        createFile("config");

        loadLanguage();

        ignoreFile = createFile("ignore");
        ignoreFileConfig = YamlConfiguration.loadConfiguration(ignoreFile);

        themesFile = createFile("themes");
        themesFileConfig = YamlConfiguration.loadConfiguration(themesFile);

        Bukkit.getPluginManager().registerEvents(new WorldChange(), this);
        Bukkit.getPluginManager().registerEvents(new JoinAndLeft(), this);
        Bukkit.getPluginManager().registerEvents(new ChatSettings(), this);
        Bukkit.getPluginManager().registerEvents(new Chatcolor(), this);
        Bukkit.getPluginManager().registerEvents(new GUI(), this);

        getCommand("chatcolor").setExecutor(new Chatcolor());
        getCommand("chatcolor").setTabCompleter(new OnTabCompleter());

        getCommand("tell").setExecutor(new Tell());
        getCommand("tell").setTabCompleter(new OnTabCompleter());

        getCommand("reply").setExecutor(new Reply());
        getCommand("reply").setTabCompleter(new OnTabCompleter());

        getCommand("me").setExecutor(new Me());
        getCommand("me").setTabCompleter(new OnTabCompleter());

        getCommand("try").setExecutor(new Try());
        getCommand("try").setTabCompleter(new OnTabCompleter());

        getCommand("flectonechat").setExecutor(new Reload());
        getCommand("flectonechat").setTabCompleter(new OnTabCompleter());

        getCommand("actions").setExecutor(new Actions());
        getCommand("ignore").setExecutor(new Ignore());
        getCommand("ignore").setTabCompleter(new OnTabCompleter());

        getCommand("ignorelist").setExecutor(new IgnoreList());

        createFlectonePlayers();

        getLogger().info("Start");
    }

    @Override
    public void onDisable() {
        getLogger().info("Stop");

    }

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

    public static FlectoneChat getInstance() {
        return instance;
    }

    public void loadLanguage(){
        List<String> defaultLangFiles = Arrays.asList("english", "russian");

        for(String lang : defaultLangFiles) createFile(lang);

        String getLanguageName = getConfig().getString("language");

        try {
            language = YamlConfiguration.loadConfiguration(createFile(getLanguageName));
            getLogger().info("Loaded " + getLanguageName + " language");

        } catch (Exception event){
            language = YamlConfiguration.loadConfiguration(createFile("english"));
            getLogger().warning("Failed to load " + getLanguageName + " language");
            getLogger().warning("Loaded default english language...");
        }

    }

    public File createFile(String fileName){
        File file = new File(getDataFolder() + File.separator + fileName + ".yml");
        checkFile(file);
        return file;
    }

    public void createFlectonePlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            Utilities.createFlectonePlayer(player);
        }
    }
}
