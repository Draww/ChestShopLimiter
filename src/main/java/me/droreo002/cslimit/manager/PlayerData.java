package me.droreo002.cslimit.manager;

import me.droreo002.cslimit.ChestShopLimiter;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerData extends YamlConfiguration {

    private static final Map<OfflinePlayer, PlayerData> configs = new HashMap<>();

    public static PlayerData getConfig(ChestShopLimiter plugin, OfflinePlayer player) {
        synchronized (configs) {
            return configs.computeIfAbsent(player, k -> new PlayerData(plugin, player));
        }
    }

    public static void remove(OfflinePlayer player) {
        configs.remove(player);
    }

    public static void removeAll() {
        synchronized (configs) {
            configs.clear();
        }
    }

    private final File file;
    private final Object saveLock = new Object();
    private final OfflinePlayer player;

    private PlayerData(ChestShopLimiter plugin, OfflinePlayer player) {
        super();
        this.player = player;
        this.file = new File(plugin.getDataFolder(), "userdata" + File.separator + player.getUniqueId() + ".yml");
        reload();
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    private void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }

    public void save() {
        synchronized (saveLock) {
            try {
                save(file);
            } catch (Exception ignore) {
            }
        }
    }

    public void setData(Player player){
        // Setup
        if (!contains("player-name")) {
            set("player-name", player.getName());
        }
        if (!contains("Info.shopCreated")) {
            set("Info.shopCreated", "{}");
        }
        if (!contains("Info.shopLimit")) {
            set("Info.shopLimit", "{}");
        }
        if (!contains("Info.shops")) {
            set("Info.shops", "{}");
        }
        if (!contains("Info.lastShopCreated")) {
            set("Info.lastShopCreated", "{}");
        }
        save();
    }
}
