package me.droreo002.cslimit.commands.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.manager.LangManager;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.entity.Player;

public class StatusCommand extends CSLCommand {

    public StatusCommand() {
        super(new String[] {"status"}, "status");
        setHasPermission(true);
        setPermission("csl.player.checkstatus");
    }

    @Override
    protected void execute(ChestShopLimiter main, Player player, String[] args) {
        if (args.length == 1) {
            success(player);
            for (String s : main.getLangManager().getMessageList(player, MessageType.PLAYER_STATUS)) {
                player.sendMessage(s);
            }
        } else {
            error(player);
            player.sendMessage(LangManager.getInstance().getMessage(MessageType.ERROR_INVALID_USAGE_STATUS));
        }
    }
}
