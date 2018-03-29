package me.droreo002.cslimit.api;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.LuckPermsHook;
import me.droreo002.cslimit.manager.PlayerData;
import me.droreo002.cslimit.utils.MessageType;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ChestSL {

    private ChestShopLimiter main;
    private static ChestSL instance;

    public static ChestSL getApi(ChestShopLimiter main) {
        if (instance == null) {
            instance = new ChestSL(main);
            return instance;
        }
        return instance;
    }

    private ChestSL(ChestShopLimiter main) {
        this.main = main;
    }

    public ChestShopLimiter getCSLPlugin() {
        return main;
    }

    /*

    The API methods and so on, goes down here!

    */

    // Get the shop created for the @param player (offline only)
    public int getShopCreated(OfflinePlayer player) {
        if (!PlayerData.getConfig(main, player).isSet("Info.shopCreated")) {
            throw new NullPointerException("There's no data for that player!");
        }
        return PlayerData.getConfig(main, player).getInt("Info.shopCreated");
    }

    // Get the shop created for the @param player (online only)
    public int getShopCreated(Player player) {
        if (!PlayerData.getConfig(main, player).isSet("Info.shopCreated")) {
            throw new NullPointerException("There's no data for that player!");
        }
        return PlayerData.getConfig(main, player).getInt("Info.shopCreated");
    }

    // Get the perms (for shop limit) of the current player (offline only)
    public String getShopLimitPerms(OfflinePlayer player) {
        PlayerData data = PlayerData.getConfig(main, player);
        if (data.isSet("Info.normalPlayerPermission")) {
            return data.getString("Info.normalPlayerPermission");
        }
        if (data.isSet("Info.LuckPermsPlayerPermission")) {
            data.getString("Info.LuckPermsPlayerPermission");
        }
        return null;
    }

    // Get the perms (for shop limit) of the current player (online only)
    public String getShopLimitPerms(Player player) {
        PlayerData data = PlayerData.getConfig(main, player);
        if (data.isSet("Info.normalPlayerPermission")) {
            return data.getString("Info.normalPlayerPermission");
        }
        if (data.isSet("Info.LuckPermsPlayerPermission")) {
            data.getString("Info.LuckPermsPlayerPermission");
        }
        return null;
    }

    // Get the limit for the player
    public int getShopLimitValue(Player player) {
        PlayerData data = PlayerData.getConfig(main, player);
        return data.getInt("Info.shopLimit");
    }

    // Setup the limit permission
    public void setupShopLimitValue(Player player) {
        PlayerData data = PlayerData.getConfig(main, player);

        if (player.hasPermission("csl.limit.unlimited")) {
            return;
        }
        // If luckperms hooked (Code Status : 100% Working)
        if (main.getHookManager().isLuckPermsHooked()) {
            ConfigurationSection cs = main.getConfigManager().getConfig().getConfigurationSection("ShopLimitLuckperms");
            User user = LuckPermsHook.get().getLuckPerms().getUserManager().getUser(player.getUniqueId());
            if (user == null) {
                player.sendMessage(main.getPrefix() + main.getLangManager().getMessage(MessageType.ERROR_CANNOT_LOAD_DATA));
                return;
            }
            if (data.isSet("normalPlayerPermission")) {
                data.set("Info.normalPlayerPermission", null);
            }
            if (!data.isSet("LuckPermsPlayerPermission")) {
                data.set("Info.LuckPermsPlayerPermission", "firstTime");
            }
            data.save();
            String currPermission = data.getString("Info.LuckPermsPlayerPermission");
            for (String s : cs.getKeys(false)) {
                if (s.equalsIgnoreCase(user.getPrimaryGroup())) {
                    if (currPermission.equalsIgnoreCase(s)) {
                        return;
                    }
                    data.set("Info.shopLimit", main.getConfigManager().getConfig().getInt("ShopLimitLuckperms." + s + ".limit"));
                    data.set("Info.LuckPermsPlayerPermission", s);
                }
            }
            data.save();
            return;
        }

        // Not hooked with luckperms ( Code Status : 100% Working lol)
        ConfigurationSection cs = main.getConfigManager().getConfig().getConfigurationSection("ShopLimit");
        if (!data.isSet("Info.normalPlayerPermission")) {
            data.set("Info.normalPlayerPermission", "firstTime");
        }
        if (data.isSet("Info.LuckPermsPlayerPermission")) {
            data.set("Info.LuckPermsPlayerPermission", null);
        }
        data.save();
        String currPermission = data.getString("Info.normalPlayerPermission");
        String permission = "csl.limit.";
        for (String s : cs.getKeys(false)) {
            if (player.hasPermission(permission + s)) {
                if (currPermission.equalsIgnoreCase(s)) {
                    return;
                }
                data.set("Info.shopLimit", main.getConfigManager().getConfig().getInt("ShopLimit." + s + ".limit"));
                data.set("Info.normalPlayerPermission", s);
            }
        }
        data.save();
    }

    // Add another value to shop created data
    public void addShopCreated(Player player, int value) {
        PlayerData data = PlayerData.getConfig(main, player);
        data.set("Info.shopCreated", getShopCreated(player) + value);
        data.save();
    }

    // Add another value to shop created data
    public void decreaseShopCreated(Player player, int value) {
        PlayerData data = PlayerData.getConfig(main, player);
        if (data.getInt("Info.shopCreated") <= 0) return;
        data.set("Info.shopCreated", getShopCreated(player) - value);
        data.save();
    }
}
