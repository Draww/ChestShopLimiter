package me.droreo002.cslimit.commands;

import com.earth2me.essentials.User;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class ConsoleCommand implements CommandExecutor {

    private ChestShopLimiter main;

    public ConsoleCommand(ChestShopLimiter main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(main.getLangManager().getMessage(MessageType.CONSOLE_ONLY));
            return true;
        }
        if (args.length > 0) {
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "help":
                        for (String s : main.getLangManager().getMessageList(MessageType.HELP)) {
                            sender.sendMessage(s);
                        }
                        return true;
                    case "check":
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
                        return true;
                    case "reload":
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RELOAD));
                        return true;
                    case "reset":
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RESET));
                        return true;
                    default:
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_UNKNOW_COMMAND));
                        return true;
                }
            }
            
            // Common commands
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("check")) {
                    if (args.length > 2) {
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String name = args[1];
                    Player target = Bukkit.getPlayerExact(name);
                    if (target == null) {
                        User user = main.getEssentials().getUser(name);
                        if (user == null) {
                            sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                            return true;
                        }
                        OfflinePlayer off = Bukkit.getOfflinePlayer(user.getConfigUUID());
                        main.getLangManager().sendCheckFormat(sender, off);
                        return true;
                    }
                    main.getLangManager().sendCheckFormat(sender, target);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!sender.hasPermission("csl.admin.reset")) {
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                        return true;
                    }
                    if (args.length > 2) {
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String name = args[1];
                    Player target = Bukkit.getPlayerExact(name);
                    if (target == null) {
                        User user = main.getEssentials().getUser(name);
                        if (user == null) {
                            sender.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                            return true;
                        }
                        OfflinePlayer off = Bukkit.getOfflinePlayer(user.getConfigUUID());
                        main.getApi().resetShopCreated(off);
                        sender.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, off.getPlayer()));
                        return true;
                    }
                    sender.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, target));
                    target.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET_OTHER).replaceAll("%player", "CONSOLE"));
                    main.getApi().resetShopCreated(target);
                    return true;
                }
            }
        } else {
            sender.sendMessage(main.getLangManager().getMessage(MessageType.ABOUT));
            return true;
        }
        return false;
    }
}
