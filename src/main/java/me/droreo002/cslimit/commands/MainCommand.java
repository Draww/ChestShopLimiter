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
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "help":
                        for (String s : main.getLangManager().getMessageList(MessageType.HELP)) {
                            player.sendMessage(s);
                        }
                        return true;
                    case "check":
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
                        return true;
                    case "reload":
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RELOAD));
                        return true;
                    case "reset":
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RESET));
                        return true;
                    default:
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_UNKNOW_COMMAND));
                        return true;
                }
            }

            // Common commands (Ignore the warning. If you use IntelliJ)
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
                        main.getLangManager().sendCheckFormat(player, off);
                        return true;
                    }
                    main.getLangManager().sendCheckFormat(player, target);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!player.hasPermission("csl.admin.reset")) {
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
                        main.getApi().resetShopCreated(off);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, off.getPlayer()));
                        return true;
                    }
                    player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, target));
                    main.getApi().resetShopCreated(target);
                    target.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET_OTHER, player));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!player.hasPermission("csl.admin.reload")) {
                        player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                        return true;
                    }
                    if (args.length > 2) {
                        player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String type = args[1];
                    switch (type.toLowerCase()) {
                        case "config":
                            main.getConfigManager().reloadConfig();
                            player.sendMessage(main.getLangManager().getMessage(MessageType.CONFIG_RELOADED));
                            return true;
                        case "lang":
                            main.getLangManager().reloadLangFile();
                            player.sendMessage(main.getLangManager().getMessage(MessageType.LANG_RELOADED));
                            return true;
                        default:
                            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_RELOAD_TYPE));
                            return true;
                    }
                }
            }
        } else {
            player.sendMessage(main.getLangManager().getMessage(MessageType.ABOUT));
            return true;
        }
        return true;
    }
}
