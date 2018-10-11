package me.droreo002.cslimit.hook.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.hook.ChestShopHook;
import me.droreo002.cslimit.manager.PlayerData;
import me.droreo002.cslimit.utils.MessageType;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class LuckPermsHook implements ChestShopHook {

    private LuckPermsApi luckPerms;

    @Override
    public String getPluginName() {
        return "LuckPerms";
    }

    @Override
    public boolean process() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = LuckPerms.getApi();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hookSuccess() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("       Successfully hooked into luckperms!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public void hookFailed() {
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
        Bukkit.getLogger().info("      Cannot hook into LuckPerms!. Plugin cannot be found!");
        Bukkit.getLogger().info("[ ! ] ___o0o [ ChestShopLimiter ] o0o___ [ ! ]");
    }

    @Override
    public boolean disablePluginIfNotFound() {
        return false;
    }

    public LuckPermsApi getLuckPerms() {
        return luckPerms;
    }

    public void setupShopLimitValue(PlayerData data, Player player, ChestShopLimiter main) {
        ConfigurationSection cs = main.getConfigManager().getConfig().getConfigurationSection("ShopLimitLuckperms");
        User user = getLuckPerms().getUser(player.getUniqueId());
        if (user == null) {
            player.sendMessage(main.getPrefix() + main.getLangManager().getMessage(MessageType.ERROR_CANNOT_LOAD_DATA));
            return;
        }
        if (data.isSet("Info.normalPlayerPermission")) {
            data.set("Info.normalPlayerPermission", null);
        }
        if (!data.isSet("Info.LuckPermsPlayerPermission")) {
            data.set("Info.LuckPermsPlayerPermission", "firstTime");
        }
        data.save();
        String currPermission = data.getString("Info.LuckPermsPlayerPermission");
        for (String s : cs.getKeys(false)) {
            if (s.equalsIgnoreCase(user.getPrimaryGroup())) {
                if (currPermission.equalsIgnoreCase(s)) {
                        /*
                        Make a checker so it wont be laggy
                         */
                    if (data.isSet("Info.shopLimit")) {
                        int shopLimit = main.getConfigManager().getConfig().getInt("ShopLimitLuckperms." + s + ".limit");
                        int shopLimitPlayer = data.getInt("Info.shopLimit");
                            /*
                            That mean the shop limit is different than the default one
                             */
                        if (shopLimit != shopLimitPlayer) {
                            data.set("Info.shopLimit", shopLimit);
                            data.save();
                        }
                    }
                    return;
                }
                data.set("Info.shopLimit", main.getConfigManager().getConfig().getInt("ShopLimitLuckperms." + s + ".limit"));
                data.set("Info.LuckPermsPlayerPermission", s);
                break;
            }
        }
        data.save();
    }
}
