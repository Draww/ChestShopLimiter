package me.droreo002.cslimit.hook;

public class HookManager {

    private static HookManager instance;
    private boolean isLuckPerms;

    private HookManager() {

    }

    public static HookManager get() {
        if (instance == null) {
            instance = new HookManager();
            return instance;
        }
        return instance;
    }

    public boolean isLuckPermsHooked() {
        return isLuckPerms;
    }

    public void setLuckPermsHooked(boolean luckPerms) {
        isLuckPerms = luckPerms;
    }
}
