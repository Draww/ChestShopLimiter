package me.droreo002.cslimit.commands;

import me.droreo002.cslimit.ChestShopLimiter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public abstract class CSLCommand {

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
    /*
    Variables
     */
    private String usage;
    private String permission;
    private String[] command;
    private String name;
    private int length;
    private boolean hasMoreThanOne;
    private boolean hasPermission;
    ChestShopLimiter main;

    public CSLCommand(String[] command, String name) {
        this.main = ChestShopLimiter.get();
        FileConfiguration config = main.getConfigManager().getConfig();
        String s1 = config.getString("CommandSound.success.sound");
        String s2 = config.getString("CommandSound.failure.sound");
        try {
            this.success = Sound.valueOf(s1);
            this.fail = Sound.valueOf(s2);
        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(main);
            Bukkit.getLogger().warning("SOUND TYPE WITH THE NAME OF (" + s1 + " or " + s2 + ") CANNOT BE FOUND!, PLUGIN WILL BE DISABLED!");
        }

        this.suc_volume = config.getInt("CommandSound.success.volume");
        this.suc_pitch = config.getInt("CommandSound.success.pitch");

        this.fail_volume = config.getInt("CommandSound.failure.volume");
        this.fail_pitch = config.getInt("CommandSound.failure.pitch");

        this.command = command;
        this.length = command.length;
        this.name = name;

        // Setup
        register();
    }

    private void register() {
        System.out.println("Registering command with name : " + name);
        synchronized (ChestShopLimiter.get().getCommands()) {
            ChestShopLimiter.get().getCommands().putIfAbsent(name.toLowerCase(), this);
        }
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getCommand() {
        return command;
    }

    public int getLength() {
        return length;
    }

    public boolean isHasMoreThanOne() {
        return hasMoreThanOne;
    }

    public void setHasMoreThanOne(boolean hasMoreThanOne) {
        this.hasMoreThanOne = hasMoreThanOne;
    }

    public String getName() {
        return name;
    }

    protected void error(Player player) {
        player.playSound(player.getLocation(), fail, 100, 1);
    }

    protected void execute(ChestShopLimiter main, Player player, String[] args) {}

    protected void success(Player player) {
        player.playSound(player.getLocation(), success, 100, 0);
    }

    public boolean isHasPermission() {
        return hasPermission;
    }
}
