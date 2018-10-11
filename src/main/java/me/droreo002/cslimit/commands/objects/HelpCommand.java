package me.droreo002.cslimit.commands.objects;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.CSLCommand;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.entity.Player;

public class HelpCommand extends CSLCommand {

    public HelpCommand() {
        super(new String[] {"help"}, "help");
    }

    @Override
    protected void execute(ChestShopLimiter main, Player player, String[] args) {
        success(player);
        for (String s : main.getLangManager().getMessageList(MessageType.HELP)) {
            player.sendMessage(s);
        }
    }
}
