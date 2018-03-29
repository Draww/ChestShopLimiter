package me.droreo002.cslimit.listener;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private ChestShopLimiter main;

    public PlayerJoinListener(ChestShopLimiter main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData data = PlayerData.getConfig(main, player);
        data.setData(player);

        main.getApi().setupShopLimitValue(player);
    }
}
