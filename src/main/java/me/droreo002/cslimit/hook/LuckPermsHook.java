package me.droreo002.cslimit.hook;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;

public class LuckPermsHook {

    private LuckPermsApi luckPerms;
    private static LuckPermsHook instance;

    private LuckPermsHook() {
        luckPerms = LuckPerms.getApi();
    }

    public static LuckPermsHook get() {
        if (instance == null) {
            instance = new LuckPermsHook();
            return instance;
        }
        return instance;
    }

    public LuckPermsApi getLuckPerms() {
        return luckPerms;
    }
}
