package com.backslide999.positionalteleport;

import com.backslide999.positionalteleport.commands.Tppos;
import com.backslide999.positionalteleport.commands.executors.TpposListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public class PositionalTeleportPlugin extends JavaPlugin {

    private static PositionalTeleportPlugin instance = null;
    private Logger logger = null;

    @Override
    public void onEnable() {
        instance = this;
        this.logger = this.getLogger();

        // Read config file
        logger.info("Reading Config File.");
        FileConfigurationOptions config = getConfig().options().copyDefaults(true);
        saveConfig();

        // Register Commands
        logger.info("Registering Commands.");
        this.getCommand("tppos").setExecutor(new TpposListener());


    }

    public void reload() {
        this.reloadConfig();
        FileConfigurationOptions config = getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {}

    public Boolean fetchConfigBoolean (final String path){
        return getConfig().getBoolean(path);
    }

    public Integer fetchConfigInteger (final String path){
        return getConfig().getInt(path);
    }

    public Object fetchConfigObject (final String path){
        return getConfig().get(path);
    }

    public String fetchConfigString (final String path){
        return getConfig().getString(path);
    }

    public List<String> fetchConfigStringList(final String path){
        return getConfig().getStringList(path);
    }

    public static PositionalTeleportPlugin getInstance() {
        return instance;
    }

    public void sendPlayerInfo(CommandSender sender, List<String> messages){
        messages.forEach( message -> this.sendPlayerInfo(sender, message));
    }

    public void sendPlayerInfo(CommandSender sender, String message){
        String line = "";
        if(fetchConfigBoolean("messages.prefix")) {
            line = ChatColor.WHITE + "[" +
                    ChatColor.GREEN + "TPPOS" +
                    ChatColor.WHITE + "] " +
                    ChatColor.YELLOW;
        }
        line += message;
        sender.sendMessage(line);
    }

    public void sendPlayerWarning(CommandSender sender, List<String> messages){
        messages.forEach( message -> this.sendPlayerWarning(sender, message));
    }

    public void sendPlayerWarning(CommandSender sender, String message){
        String line = "";
        if(fetchConfigBoolean("messages.prefix")) {
            line = ChatColor.WHITE + "[" +
                    ChatColor.GREEN + "TPPOS" +
                    ChatColor.WHITE + "] " +
                    ChatColor.RED;
        }
        line += message;
        sender.sendMessage(line);
    }

}
