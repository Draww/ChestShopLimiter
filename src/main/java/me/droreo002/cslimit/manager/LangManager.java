package me.droreo002.cslimit.manager;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.utils.MessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import sun.plugin2.message.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LangManager {

    private ChestShopLimiter plugin = (ChestShopLimiter) ChestShopLimiter.getPlugin(ChestShopLimiter.class);
    private FileConfiguration MainConfig;
    private File MainConfigFile;
    private static LangManager instance;

    private LangManager() {

    }

    public static LangManager getInstance() {
        if (instance == null) {
            instance = new LangManager();
            return instance;
        }
        return instance;
    }

    public String getMessage(MessageType type) {
        switch (type) {
            case SHOP_CREATED_RESET_OTHER:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.shop-created-reset-other"));
            case ERROR_INVALID_USAGE_CHECK:
                return colorized(plugin.getPrefix() + getLangFile().getString("error.usage.command-check"));
            case ERROR_UNKNOW_COMMAND:
                return colorized(plugin.getPrefix() + getLangFile().getString("error.usage.command-unknown"));
            case CONSOLE_ONLY:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.console-only"));
            case TOO_MUCH_ARGS:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.too-much-args"));
            case PLAYER_ONLY:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.player-only"));
            case ABOUT:
                return colorized(plugin.getPrefix() + "Created by &c@DrOreo002 &f, enjoy my plugin!");
            case NO_PERMISSION:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.no-permission"));
            case ERROR_CANNOT_LOAD_DATA:
                return colorized(plugin.getPrefix() + getLangFile().getString("error.cannot-load-data"));
            case ERROR_PLAYER_NEVER_PLAYED:
                return colorized(plugin.getPrefix() + getLangFile().getString("error.player-never-played"));
            case MISC_TELEPORT_BUTTON:
                return colorized(getLangFile().getString("misc.teleport-button-text"));
            case MISC_TELEPORT_BUTTON_HOVER:
                return colorized(getLangFile().getString("misc.teleport-button-hover-text"));
        }
        return "Error on getting message. Please contact Dev!";
    }

    // Used if the lang contains variable
    public String getMessage(MessageType type, Player player) {
        switch (type) {
            case SHOP_CREATED:
                return plugin.getPrefix() + colorized(getLangFile().getString("normal.shop-created")
                .replaceAll("%created", Integer.toString(plugin.getApi().getShopCreated(player)))
                .replaceAll("%max", Integer.toString(plugin.getApi().getShopLimitValue(player))));
            case ERROR_LIMIT_REACHED:
                return plugin.getPrefix() + colorized(getLangFile().getString("error.limit-reached")
                        .replaceAll("%created", Integer.toString(plugin.getApi().getShopCreated(player)))
                        .replaceAll("%max", Integer.toString(plugin.getApi().getShopLimitValue(player))));
            case SHOP_REMOVED:
                return plugin.getPrefix() + colorized(getLangFile().getString("normal.shop-removed")
                        .replaceAll("%created", Integer.toString(plugin.getApi().getShopCreated(player)))
                        .replaceAll("%max", Integer.toString(plugin.getApi().getShopLimitValue(player))));
            case SHOP_CREATED_RESET:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.shop-created-reset")
                .replaceAll("%player", player.getName()));
            case SHOP_CREATED_RESET_OTHER:
                return colorized(plugin.getPrefix() + getLangFile().getString("normal.shop-created-reset-other")
                .replaceAll("%player", player.getName()));
            default:
                return getMessage(type);
        }
    }

    public List<String> getMessageList(MessageType type) {
        // If nothing is returned. It will use this
        List<String> temp = new ArrayList<>();
        temp.add("Error on getting message. Please contact Dev!");
        switch (type) {
            case HELP:
                // Help has no placeholder
                List<String> help = new ArrayList<>();
                for (String s : getLangFile().getStringList("list.help-message")) {
                help.add(colorized(s));
            }
            return help;
        }
        return temp;
    }

    // Get the checkformat for online player
    /*
    public void sendCheckFormat(Player player, Player target) {
        for (String s : getLangFile().getStringList("list.check-format")) {
            if (s.contains("%lastshop")) {
                String result = s.replaceAll("%lastshop", "");
                TextComponent lastShopCreated_first = new TextComponent(colorized(result));
                TextComponent lastShopCreated_second = new TextComponent(colorized(getMessage(MessageType.MISC_TELEPORT_BUTTON)));
                BaseComponent[] base = new BaseComponent[] {
                        new TextComponent("Hello World")
                };
                lastShopCreated_second.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, base));
                lastShopCreated_first.addExtra(lastShopCreated_second);
                player.spigot().sendMessage(lastShopCreated_first);
                continue;
            }
            String result = s.replaceAll("%player", player.getName())
                    .replaceAll("%uuid", player.getUniqueId().toString());
            player.sendMessage(colorized(result));
        }
    }
    */

    // Send the check format to specified player
    public void sendCheckFormat(CommandSender sender, Player target) {
        Location lastCreated = plugin.getApi().getLastShopCreated(target);
        for (String s : getLangFile().getStringList("list.check-format")) {
            if (s.contains("%lastshop")) {
                String result = s.replaceAll("%lastshop", "");
                TextComponent lastShopCreated_first = new TextComponent(colorized(result));
                TextComponent lastShopCreated_second = new TextComponent(colorized(getMessage(MessageType.MISC_TELEPORT_BUTTON)));
                BaseComponent[] base = new BaseComponent[] {
                        new TextComponent(colorized(getMessage(MessageType.MISC_TELEPORT_BUTTON_HOVER)))
                };
                lastShopCreated_second.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, base));
                lastShopCreated_first.addExtra(lastShopCreated_second);
                // Setup the click event
                if (lastCreated != null) {
                    if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
                        lastShopCreated_second.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + Math.round(lastCreated.getX()) + " " + Math.round(lastCreated.getY() + 2.5D) + " " + Math.round(lastCreated.getZ()) + " " + Math.round(lastCreated.getYaw()) + " " + Math.round(lastCreated.getPitch())));
                    } else  {
                        lastShopCreated_second.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp " + sender.getName() + Math.round(lastCreated.getX()) + " " + Math.round(lastCreated.getY() + 2.5D) + " " + Math.round(lastCreated.getZ()) + " " + Math.round(lastCreated.getYaw()) + " " + Math.round(lastCreated.getPitch())));
                    }
                }
                sender.spigot().sendMessage(lastShopCreated_first);
                continue;
            }
            String result = s.replaceAll("%player", target.getName())
                    .replaceAll("%uuid", target.getUniqueId().toString())
                    .replaceAll("%shopcreated", String.valueOf(plugin.getApi().getShopCreated(target)))
                    .replaceAll("%shoplimit", String.valueOf(plugin.getApi().getShopLimitValue(target)));
            sender.sendMessage(colorized(result));
        }
    }

    // Send the check format to specified player
    public void sendCheckFormat(CommandSender sender, OfflinePlayer target) {
        Location lastCreated = plugin.getApi().getLastShopCreated(target);
        for (String s : getLangFile().getStringList("list.check-format")) {
            if (s.contains("%lastshop")) {
                String result = s.replaceAll("%lastshop", "");
                TextComponent lastShopCreated_first = new TextComponent(colorized(result));
                TextComponent lastShopCreated_second = new TextComponent(colorized(getMessage(MessageType.MISC_TELEPORT_BUTTON)));
                BaseComponent[] base = new BaseComponent[] {
                        new TextComponent(colorized(getMessage(MessageType.MISC_TELEPORT_BUTTON_HOVER)))
                };
                lastShopCreated_second.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, base));
                lastShopCreated_first.addExtra(lastShopCreated_second);
                // Setup the click event
                if (lastCreated != null) {
                    if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
                        lastShopCreated_second.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + Math.round(lastCreated.getX()) + " " + Math.round(lastCreated.getY() + 2.5D) + " " + Math.round(lastCreated.getZ()) + " " + Math.round(lastCreated.getYaw()) + " " + Math.round(lastCreated.getPitch())));
                    } else  {
                        lastShopCreated_second.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp " + sender.getName() + Math.round(lastCreated.getX()) + " " + Math.round(lastCreated.getY() + 2.5D) + " " + Math.round(lastCreated.getZ()) + " " + Math.round(lastCreated.getYaw()) + " " + Math.round(lastCreated.getPitch())));
                    }
                }
                sender.spigot().sendMessage(lastShopCreated_first);
                continue;
            }
            String result = s.replaceAll("%player", target.getName())
                    .replaceAll("%uuid", target.getUniqueId().toString())
                    .replaceAll("%shopcreated", String.valueOf(plugin.getApi().getShopCreated(target)))
                    .replaceAll("%shoplimit", String.valueOf(plugin.getApi().getShopLimitValue(target)));
            sender.sendMessage(colorized(result));
        }
    }

    private String colorized(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void setup() {
        if (!plugin.getConfigManager().getConfig().isSet("Language")) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new NullPointerException("Language is not set!. Please set it otherwise the plugin wont work!. Disabling the plugin");
        }
        String fileName = plugin.getConfigManager().getConfig().getString("Language") + ".yml";
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        MainConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!MainConfigFile.exists()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new NullPointerException("Cannot find the lang file!. Make sure the file name is correct!");
        }
        MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
    }

    public FileConfiguration getLangFile()
    {
        if (MainConfig == null) {
            reloadLangFile();
        }
        return MainConfig;
    }

    public void saveLangFile()
    {
        if (MainConfig == null) {
            throw new NullArgumentException("Cannot save a non-existant file!");
        }
        try
        {
            MainConfig.save(MainConfigFile);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
        }
    }

    public void reloadLangFile()
    {
        String fileName = plugin.getConfigManager().getConfig().getString("Language") + ".yml";
        MainConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!MainConfigFile.exists()) {
            /*
            plugin.saveResource("EN_lang.yml", false);
            */
            Bukkit.getPluginManager().disablePlugin(plugin);
            throw new NullPointerException("Cannot find the lang file!. Make sure the file name is correct!");
        }
        MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
        InputStream configData = plugin.getResource("EN_lang.yml");
        if (configData != null) {
            MainConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configData)));
        }
    }
}
