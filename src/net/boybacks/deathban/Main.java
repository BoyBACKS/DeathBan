package net.boybacks.deathban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.GREEN + " Plugin DeathBan jest aktywny\n\n");
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.RED + " Plugin DeathBan jest nieaktywny\n\n");
        this.saveDefaultConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        if(e.getEntity() instanceof Player) {
            if (!(player.hasPermission("deathban.bypass"))) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        this.getConfig().getString("command").replace("{PLAYER}", player.getName()));
            }
            else if (player.hasPermission("deathban.bypass")){
                player.sendMessage(ChatColor.GREEN + this.getConfig().getString("message"));
            }
        }
    }
}
