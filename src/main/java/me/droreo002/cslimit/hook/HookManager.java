package me.droreo002.cslimit.hook;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import me.droreo002.cslimit.hook.objects.LuckPermsHook;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Logger;

public class HookManager {

    private boolean isLuckPerms;
    private boolean isPlaceHolderAPI;
    private boolean isEssentials;
    private boolean isCMI;
    private Logger logger = Bukkit.getLogger();

    private Map<String, ChestShopHook> hookMap = new HashMap<>();

    public HookManager() {
        // Setup hook here
        if (ChestShopLimiter.get().getHookManager() != null) {
            throw new IllegalStateException("Cannot create a new hook manager instance anymore!. Please get this instance on the ChestShopLimiter's main class!");
        }
        // Setup all hooks here
        FileConfiguration config = ChestShopLimiter.get().getConfigManager().getConfig();
        if (config.getBoolean("Dependency.Essentials")) {
            if (registerHook("Essentials", new EssentialsHook())) {
                setEssentials(true);
            }
        }
        if (config.getBoolean("Dependency.CMI")) {
            if (!isEssentials()) {
                if (registerHook("CMI", new CMIHook())) {
                    setCMI(true);
                }
            }
        }
        // Just in case
        if (!isCMI && !isEssentials) {
            Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.get());
            throw new IllegalStateException("Essentials or CMI dependency must be enabled an installed on the server if you want to run this plugin!");
        }
        if (config.getBoolean("Dependency.LuckPerms")) {
            if (registerHook("LuckPerms", new LuckPermsHook())) {
                setLuckPerms(true);
            }
        }
    }

    private boolean registerHook(String plugin, ChestShopHook hook) {
        if (hook.process()) {
            hook.hookSuccess();
            hookMap.put(plugin, hook);
            return true;
        } else {
            if (hook.disablePluginIfNotFound()) {
                hook.hookFailed();
                Bukkit.getPluginManager().disablePlugin(ChestShopLimiter.get());
            } else {
                hook.hookFailed();
                return false;
            }
        }
        return false;
    }

    public Map<String, ChestShopHook> getHookMap() {
        return hookMap;
    }

    private Logger getLogger() {
        return logger;
    }

    public boolean isLuckPermsHooked() {
        return isLuckPerms;
    }

    public boolean isLuckPerms() {
        return isLuckPerms;
    }

    public boolean isPlaceHolderAPI() {
        return isPlaceHolderAPI;
    }

    public boolean isEssentials() {
        return isEssentials;
    }

    public boolean isCMI() {
        return isCMI;
    }

    public void setLuckPerms(boolean luckPerms) {
        isLuckPerms = luckPerms;
    }

    public void setPlaceHolderAPI(boolean placeHolderAPI) {
        isPlaceHolderAPI = placeHolderAPI;
    }

    public void setEssentials(boolean essentials) {
        isEssentials = essentials;
    }

    public void setCMI(boolean CMI) {
        isCMI = CMI;
    }

}
