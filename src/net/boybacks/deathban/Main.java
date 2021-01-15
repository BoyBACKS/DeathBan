package net.boybacks.deathban;

import net.boybacks.deathban.event.DeathEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[DeathBan]" + ChatColor.GREEN + " Plugin DeathBan jest aktywny");
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[DeathBan]" + ChatColor.RED + " Plugin DeathBan jest nieaktywny");
    }
}