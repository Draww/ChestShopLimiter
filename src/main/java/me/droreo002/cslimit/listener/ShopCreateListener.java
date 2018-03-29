package me.droreo002.cslimit.listener;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.PlayerData;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.material.Sign;

public class ShopCreateListener implements Listener {

    private ChestShopLimiter main;

    public ShopCreateListener(ChestShopLimiter main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreate(PreShopCreationEvent e) {
        if (!e.isCancelled()) {
            Player player = e.getPlayer();
            String[] line = e.getSignLines();
            // Ignore if the shop created is admin shop
            if (ChestShopSign.isAdminShop(line[0])) {
                return;
            }
            // Ignored if the player has unlimited perms
            if (player.hasPermission("csl.limit.unlimited")) {
                return;
            }
            if (main.getApi().getShopCreated(player) >= main.getApi().getShopLimitValue(player)) {
                player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_LIMIT_REACHED, player));
                e.setOutcome(PreShopCreationEvent.CreationOutcome.NO_PERMISSION);
                return;
            }
            e.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
            main.getApi().addShopCreated(player, 1);
            player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED, player));

            // Add lastshop created

            PlayerData data = PlayerData.getConfig(main, player);
            if (!data.isSet("lastShopCreated.location")) {
                Sign signMaterial = (Sign) e.getSign().getBlock().getState().getData();
                data.set("lastShopCreated.location", e.getSign().getBlock().getRelative(signMaterial.getAttachedFace()).getLocation());
                data.save();
            }
        }
    }
}
