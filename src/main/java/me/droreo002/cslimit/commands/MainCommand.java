package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class MainCommand implements CommandExecutor {

    private ChestShopLimiter main;

    public MainCommand(ChestShopLimiter main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getLangManager().getMessage(MessageType.PLAYER_ONLY));
            return true;
        }
        Player player = (Player) sender;
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "help":
                    for (String s : main.getLangManager().getMessageList(MessageType.HELP)) {
                        player.sendMessage(s);
                    }
                    return true;
            }
        } else {
            player.sendMessage(main.getLangManager().getMessage(MessageType.ABOUT));
            return true;
        }

        // Common commands
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("check")) {
                if (!player.hasPermission("csl.admin.check")) {
                    player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                    return true;
                }
                if (args.length > 2) {
                    player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                    return true;
                }
                String name = args[1];
                Player target = Bukkit.getPlayerExact(name);
                if (target == null) {
                    OfflinePlayer off = Bukkit.getOfflinePlayer(name);
                    if (!off.hasPlayedBefore()) {
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                        return true;
                    }
                    main.getLangManager().sendCheckFormat(player, target);
                    return true;
                }
                main.getLangManager().sendCheckFormat(player, target);
            }
        }
        return true;
    }
}
