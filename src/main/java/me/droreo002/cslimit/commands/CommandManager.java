package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.commands.objects.*;
import me.droreo002.cslimit.utils.MessageType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabExecutor {

    private ChestShopLimiter main;
    private final Collection<String> completions = new ArrayList<>();

    public CommandManager(ChestShopLimiter main) {
        this.main = main;
         /*
        Tabs
         */
        completions.add("help");
        completions.add("check");
        completions.add("reload");
        completions.add("status");
        completions.add("reset");
        completions.add("save-data");
        /*
        Register commands
         */
        new HelpCommand();
        new CheckCommand();
        new ReloadCommand();
        new SaveDataCommand();
        new ResetCommand();
        new StatusCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getLangManager().getMessage(MessageType.PLAYER_ONLY));
            return true;
        }
        Player player = (Player) sender;
        if (args.length > 0) {
            int success = 0;
            for (Map.Entry ent : main.getCommands().entrySet()) {
                CSLCommand clazz = (CSLCommand) ent.getValue();
                if (clazz.getCommand()[0].equalsIgnoreCase(args[0])) {
                    if (clazz.isHasPermission()) {
                        if (!player.hasPermission(clazz.getPermission())) {
                            clazz.error(player);
                            player.sendMessage(main.getLangManager().getMessage(MessageType.NO_PERMISSION));
                            return true;
                        }
                    }
                    clazz.execute(main, player, args);
                    success++;
                    break;
                }
            }
            if (success == 0) {
                player.sendMessage(main.getLangManager().getMessage(MessageType.ERROR_UNKNOW_COMMAND));
                return true;
            }
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
            player.sendMessage(main.getLangManager().getMessage(MessageType.ABOUT));
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return createReturnList(new ArrayList<>(completions), args[0]);
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("config");
                toReturn.add("lang");
                return createReturnList(toReturn, args[1]);
            }
            return null;
        }
        else {
            return null;
        }
    }

    private List<String> createReturnList(List<String> list, String string) {
        if (string.equals("")) return list;

        List<String> returnList = new ArrayList<>();
        for (String item : list) {
            if (item.toLowerCase().startsWith(string.toLowerCase())) {
                returnList.add(item);
            }
        }
        return returnList;
    }
}
