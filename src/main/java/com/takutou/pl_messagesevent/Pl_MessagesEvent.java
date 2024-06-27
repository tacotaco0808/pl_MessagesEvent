package com.takutou.pl_messagesevent;

import org.bukkit.plugin.java.JavaPlugin;

public final class Pl_MessagesEvent extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Hello World!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
