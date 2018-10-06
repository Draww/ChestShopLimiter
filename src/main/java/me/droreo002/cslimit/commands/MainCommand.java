package me.droreo002.cslimit.commands;

import com.earth2me.essentials.User;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.manager.LangManager;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class MainCommand implements CommandExecutor {

    private ChestShopLimiter main;

    /*
    Sounds
     */
    private Sound success;
    private Sound fail;
    /*
    Volumes & pitch
     */
    private float suc_volume;
    private float suc_pitch;
    private float fail_volume;
    private float fail_pitch;

    public MainCommand(ChestShopLimiter main) {
        this.main = main;

        FileConfiguration config = main.getConfigManager().getConfig();
        String s1 = config.getString("CommandSound.success.sound");
        String s2 = config.getString("CommandSound.failure.sound");
        try {
            success = Sound.valueOf(s1);
            fail = Sound.valueOf(s2);
        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(main);
            Bukkit.getLogger().warning("SOUND TYPE WITH THE NAME OF (" + s1 + " and " + s2 + ") PLUGIN WILL BE DISABLED!");
        }

        suc_volume = config.getInt("CommandSound.success.volume");
        suc_pitch = config.getInt("CommandSound.success.pitch");

        fail_volume = config.getInt("CommandSound.failure.volume");
        fail_pitch = config.getInt("CommandSound.failure.pitch");
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
                        playSuccess(player);
                        for (String s : main.getLangManager().getMessageList(MessageType.HELP)) {
                            player.sendMessage(s);
                        }
                        return true;
                    case "check":
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
                        return true;
                    case "reload":
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RELOAD));
                        return true;
                    case "status":
                        if (!player.hasPermission("csl.player.checkstatus")) {
                            player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                            playFailure(player);
                            return true;
                        }
                        playSuccess(player);
                        for (String s : main.getLangManager().getMessageList(player, MessageType.PLAYER_STATUS)) {
                            player.sendMessage(s);
                        }
                        return true;
                    case "reset":
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RESET));
                        return true;
                    default:
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_UNKNOW_COMMAND));
                        return true;
                }
            }

            // Common commands (Ignore the warning. If you use IntelliJ)
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("check")) {
                    if (!player.hasPermission("csl.admin.check")) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                        return true;
                    }
                    if (args.length > 2) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String name = args[1];
                    Player target = Bukkit.getPlayerExact(name);
                    if (target == null) {
                        User user = main.getEssentials().getUser(name);
                        if (user == null) {
                            playFailure(player);
                            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                            return true;
                        }
                        playSuccess(player);
                        OfflinePlayer off = Bukkit.getOfflinePlayer(user.getConfigUUID());
                        main.getLangManager().sendCheckFormat(player, off);
                        return true;
                    }
                    playSuccess(player);
                    main.getLangManager().sendCheckFormat(player, target);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!player.hasPermission("csl.admin.reset")) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                        return true;
                    }
                    if (args.length > 2) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String name = args[1];
                    Player target = Bukkit.getPlayerExact(name);
                    if (target == null) {
                        User user = main.getEssentials().getUser(name);
                        if (user == null) {
                            playFailure(player);
                            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                            return true;
                        }
                        playSuccess(player);
                        OfflinePlayer off = Bukkit.getOfflinePlayer(user.getConfigUUID());
                        main.getApi().resetShopCreated(off);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, off.getPlayer()));
                        return true;
                    }
                    playSuccess(player);
                    player.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET, target));
                    main.getApi().resetShopCreated(target);
                    target.sendMessage(main.getLangManager().getMessage(MessageType.SHOP_CREATED_RESET_OTHER, player));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!player.hasPermission("csl.admin.reload")) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                        return true;
                    }
                    if (args.length > 2) {
                        playFailure(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
                        return true;
                    }
                    String type = args[1];
                    switch (type.toLowerCase()) {
                        case "config":
                            playSuccess(player);
                            main.getConfigManager().reloadConfig();
                            player.sendMessage(main.getLangManager().getMessage(MessageType.CONFIG_RELOADED));
                            return true;
                        case "lang":
                            playSuccess(player);
                            main.getLangManager().reloadLangFile();
                            player.sendMessage(main.getLangManager().getMessage(MessageType.LANG_RELOADED));
                            return true;
                        default:
                            playFailure(player);
                            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_RELOAD_TYPE));
                            return true;
                    }
                }
            }
        } else {
            playSuccess(player);
            player.sendMessage(main.getLangManager().getMessage(MessageType.ABOUT));
            return true;
        }
        return true;
    }

    private void playSuccess(Player player) {
        player.playSound(player.getLocation(), success, suc_volume, suc_pitch);
    }

    private void playFailure(Player player) {
        player.playSound(player.getLocation(), fail, fail_volume, fail_pitch);
    }
}
