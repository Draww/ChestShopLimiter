package me.droreo002.cslimit.commands.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.hook.objects.CMIHook;
import me.droreo002.cslimit.hook.objects.EssentialsHook;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CheckCommand extends CSLCommand {

    public CheckCommand() {
        super(new String[] {"check"}, "check");
        setHasPermission(true);
        setPermission("csl.admin.check");
    }

    @Override
    protected void execute(ChestShopLimiter main, Player player, String[] args) {
        if (args.length == 1) {
            error(player);
            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_CHECK));
            return;
        }
        if (args.length == 2) {
            String name = args[1];
            Player target = Bukkit.getPlayerExact(name);
            if (target == null) {
                if (main.getHookManager().isEssentials()) {
                    EssentialsHook hook = (EssentialsHook) main.getHookManager().getHookMap().get("Essentials");
                    if (hook.getUser(name) == null) {
                        success(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                        return;
                    }
                    success(player);
                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getUser(name).getConfigUUID());
                    main.getLangManager().sendCheckFormat(player, off);
                    return;
                } else {
                    // Use CMI
                    CMIHook hook = (CMIHook) main.getHookManager().getHookMap().get("CMI");
                    if (hook.getPlayerManager().getUser(name) == null) {
                        success(player);
                        player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_PLAYER_NEVER_PLAYED));
                        return;
                    }
                    success(player);
                    OfflinePlayer off = Bukkit.getOfflinePlayer(hook.getPlayerManager().getUser(name).getUniqueId());
                    main.getLangManager().sendCheckFormat(player, off);
                    return;
                }
            }
            success(player);
            main.getLangManager().sendCheckFormat(player, target);
        } else {
            error(player);
            player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
        }
    }
}
