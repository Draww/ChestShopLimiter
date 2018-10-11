package me.droreo002.cslimit.hook.objects;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import me.droreo002.cslimit.hook.ChestShopHook;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CMIHook implements ChestShopHook {

    private CMI cmi;
    private PlayerManager manager;

    @Override
    public String getPluginName() {
        return "CMI";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("CMI") != null) {
            cmi = (CMI) Bukkit.getPluginManager().getPlugin("CMI");
            manager = cmi.getPlayerManager();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("             Hooked to CMI plugin!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public void hookFailed() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("    Cannot hook into CMI!. Plugin cannot be found!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return true;
    }

    public CMI getCmi() {
        return cmi;
    }

    public PlayerManager getPlayerManager() {
        return manager;
    }
}
