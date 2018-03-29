package me.droreo002.cslimit.listener;

import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.PlayerData;
import me.droreo002.cslimit.utils.MessageType;
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
        String[] lines = e.getSign().getLines();
        if (ChestShopSign.isAdminShop(lines[0])) return;
        if (player.hasPermission("csl.limit.unlimited")) return;
        if (main.getConfigManager().getConfig().getBoolean("RefundOnShopRemove")) {
            PlayerData data = PlayerData.getConfig(main, player);
            main.getApi().decreaseShopCreated(player, 1);
            player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_REMOVED, player));

            // Remove last shop created
            if (data.isSet("lastShopCreated.location")) {
                data.set("lastShopCreated.location", null);
                data.save();
            }
        }
    }
}
