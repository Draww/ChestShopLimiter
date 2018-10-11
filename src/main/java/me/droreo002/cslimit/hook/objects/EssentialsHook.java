package me.droreo002.cslimit.hook.objects;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.droreo002.cslimit.hook.ChestShopHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssentialsHook implements ChestShopHook {

    private Essentials essentials;

    @Override
    public String getPluginName() {
        return "Essentials";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("       Successfully hooked to Essentials!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public void hookFailed() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("    Cannot hook into Essentials!. Plugin cannot be found!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return true;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public User getUser(String name) {
        return essentials.getUser(name);
    }

    public User getUser(Player name) {
        return essentials.getUser(name);
    }

    public User getUser(UUID name) {
        return essentials.getUser(name);
    }
}
