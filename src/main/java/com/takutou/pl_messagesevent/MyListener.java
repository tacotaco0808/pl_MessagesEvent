package com.takutou.pl_messagesevent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Bukkit.broadcastMessage(ChatColor.BLUE +event.getPlayer().getName()+ChatColor.WHITE+"さんJokenサーバーへようこそ！");
    }
}
