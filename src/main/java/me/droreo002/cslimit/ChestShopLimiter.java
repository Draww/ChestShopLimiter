package me.droreo002.cslimit;

import com.earth2me.essentials.Essentials;
import me.droreo002.cslimit.api.ChestSL;
import me.droreo002.cslimit.commands.ConsoleCommand;
import me.droreo002.cslimit.commands.MainCommand;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.hook.LuckPermsHook;
import me.droreo002.cslimit.listener.PlayerJoinListener;
import me.droreo002.cslimit.listener.PlayerQuitListener;
import me.droreo002.cslimit.listener.ShopCreateListener;
import me.droreo002.cslimit.listener.ShopDestroyListener;
import me.droreo002.cslimit.manager.ConfigManager;
import me.droreo002.cslimit.manager.LangManager;
import me.droreo002.cslimit.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopLimiter extends JavaPlugin {

    private ConfigManager configManager;
    private static ChestShopLimiter plugin;
    private static ChestSL api;
    private LangManager langManager;
    private HookManager hookManager;
    private Essentials essentials;
    private Metrics met;

    @Override
    public void onEnable() {
        // Use this to initialize fields

        configManager = ConfigManager.getInstance();
        configManager.setup();
        langManager = LangManager.getInstance();
        langManager.setup();
        plugin = this;
        api = ChestSL.getApi(this);
        hookManager = HookManager.get();
        if (configManager.getConfig().isSet("use-bstats")) {
            System.out.print("[CSL] Connecting to bstats");
            if (configManager.getConfig().getBoolean("use-bstats")) {
                System.out.print("[CSL] bstats connected!");
                met = new Metrics(this);
            } else {
                System.out.print("[CSL] bstats is disabled!. Disconnecting from the server...");
                met = null;
            }
        } else {
            System.out.print("[CSL] Connecting to bstats");
            configManager.getConfig().set("use-bstats", true);
            configManager.saveConfig();
            if (configManager.getConfig().getBoolean("use-bstats")) {
                System.out.print("[CSL] bstats connected!");
                met = new Metrics(this);
            } else {
                System.out.print("[CSL] bstats is disabled!. Disconnecting from the server...");
                met = null;
            }
        }

        // Use this to register command and stuff

        Bukkit.getPluginCommand("chestshoplimiter").setExecutor(new MainCommand(this));
        Bukkit.getPluginCommand("chestshoplimiterconsole").setExecutor(new ConsoleCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopCreateListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopDestroyListener(this), this);

        // Dependency check (Hard depend)
        if (Bukkit.getPluginManager().getPlugin("ChestShop") == null) {
            getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
            getLogger().warning("WARNING. PLUGIN WILL BE DISABLED BECAUSE CHESTSHOP IS NOT FOUND!");
            getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Dependency check (Soft depend)
        if (configManager.getConfig().getBoolean("LuckPermsSupport")) {
            if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                // Set hook manager to true
                hookManager.setLuckPermsHooked(true);
                getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
                getLogger().info("Successfully hooked to luckperms!");
                getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
            } else {
                Bukkit.getPluginManager().disablePlugin(this);
                getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
                getLogger().warning("CANNOT FIND LUCKPERMS. DISABLE LuckPermsSupport ON CONFIG.YML NOW!");
                getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
                return;
            }
        }
        // Dependency check (Hard depend)
        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
            getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
            getLogger().info("  ➟ ChestShopLimiter+ need Essentials or EssentialsX to work!. Please install it on your server!");
            getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            getLogger().info("Successfully hooked to Essentials!");
        }

        // Final (Will ran if dependency check is passed)

        getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        getLogger().info("  ➟ ChestShopLimiter+ has been enabled!");
        getLogger().info("  ➟ Version : " + getDescription().getVersion());
        getLogger().info("  ➟ Author : " + getDescription().getAuthors());
        getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");

        // ChestShopLimiter + : Made by @DrOreo002
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public String getPrefix() {
        return color(configManager.getConfig().getString("Prefix"));
    }

    public String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public ChestSL getApi() {
        return api;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public static ChestShopLimiter get() {
        return plugin;
    }

    public HookManager getHookManager() {
        return hookManager;
    }

    public Metrics getMet() {
        return met;
    }
}
