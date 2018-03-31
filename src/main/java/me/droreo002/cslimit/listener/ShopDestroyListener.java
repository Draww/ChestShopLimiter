package me.droreo002.cslimit.listener;

import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.PlayerData;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.material.Sign;

public class ShopDestroyListener implements Listener {

    private ChestShopLimiter main;

    public ShopDestroyListener(ChestShopLimiter main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDestroy(ShopDestroyedEvent e) {
        Player player = e.getDestroyer();
        if (player == null) return;
        String[] lines = e.getSign().getLines();
        if (ChestShopSign.isAdminShop(lines[0])) return;
        if (player.hasPermission("csl.limit.unlimited")) return;
        if (main.getConfigManager().getConfig().getBoolean("RefundOnShopRemove")) {
            PlayerData data = PlayerData.getConfig(main, player);
            main.getApi().decreaseShopCreated(player, 1);
            player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_REMOVED, player));

            // Remove last shop created
            Location loc = (Location) data.get("lastShopCreated.location");
            Sign signMaterial = (Sign) e.getSign().getBlock().getState().getData();
            if (data.isSet("lastShopCreated.location") && e.getSign().getBlock().getRelative(signMaterial.getAttachedFace()).getLocation().equals(loc)) {
                data.set("lastShopCreated.location", null);
                data.save();
            }
        }
    }
}
