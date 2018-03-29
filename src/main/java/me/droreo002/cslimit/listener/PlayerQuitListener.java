package me.droreo002.cslimit.listener;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ChestShopLimiter main;

    public PlayerQuitListener(ChestShopLimiter main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerData.remove(player);
    }
}
