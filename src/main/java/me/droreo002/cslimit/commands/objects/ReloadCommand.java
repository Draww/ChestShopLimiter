package me.droreo002.cslimit.commands.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.entity.Player;

public class ReloadCommand extends CSLCommand {

    public ReloadCommand() {
        super(new String[] {"reload"}, "reload");
        setHasPermission(true);
        setPermission("csl.admin.reload");
    }

    @Override
    protected void execute(ChestShopLimiter main, Player player, String[] args) {
        if (args.length == 1) {
            error(player);
            player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_USAGE_RELOAD));
            return;
        }
        if (args.length == 2) {
            String type = args[1];
            switch (type.toLowerCase()) {
                case "config":
                    success(player);
                    main.getConfigManager().reloadConfig();
                    player.sendMessage(main.getLangManager().getMessage(MessageType.CONFIG_RELOADED));
                    return;
                case "lang":
                    success(player);
                    main.getLangManager().reloadLangFile();
                    player.sendMessage(main.getLangManager().getMessage(MessageType.LANG_RELOADED));
                    return;
                default:
                    success(player);
                    player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_INVALID_RELOAD_TYPE));
            }
        } else {
            player.sendMessage(main.getLangManager().getMessage(MessageType.TOO_MUCH_ARGS));
            error(player);
        }
    }
}
