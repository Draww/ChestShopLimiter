package me.droreo002.cslimit.hook.objects;

import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.hook.papi.CSLPlaceholder;
import org.bukkit.Bukkit;

public class PlaceholderAPIHook implements ChestShopHook {

    @Override
    public String getPluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // Register the expansion
            new CSLPlaceholder().register();
            return true;
        }
        return false;
    }

    @Override
    public void hookSuccess() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("       Successfully hooked to PlaceholderAPI!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public void hookFailed() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("    Cannot hook into PlaceholderAPI!. Plugin cannot be found!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }
}
