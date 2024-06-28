package com.takutou.pl_messagesevent;

import org.bukkit.plugin.java.JavaPlugin;

public final class Pl_MessagesEvent extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Hello World!");
        getLogger().info("コミット共有テスト");
        this.getCommand("kit").setExecutor(new CommandKit());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
