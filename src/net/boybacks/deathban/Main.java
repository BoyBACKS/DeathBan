package net.boybacks.deathban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.GREEN + " Plugin DeathBan jest aktywny\n\n");
        getServer().getPluginManager().registerEvents(this, this);
        config();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.RED + " Plugin DeathBan jest nieaktywny\n\n");
        this.saveDefaultConfig();
    }

    public void config() {
        this.saveDefaultConfig();
        this.configPlayerData();
        this.save();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        UUID uuid = player.getUniqueId();
        int death = this.s.getInt(uuid + ".deathcout");
        if(e.getEntity() instanceof Player) {
            if (!(player.hasPermission("deathban.bypass"))) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        this.getConfig().getString("command").replace("{PLAYER}", player.getName()));
            }
            else if (player.hasPermission("deathban.bypass")){
                player.sendMessage(ChatColor.GREEN + this.getConfig().getString("message"));
            }
            this.s.set(uuid + ".deathcout", death + 1);
            this.save();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!player.hasPlayedBefore()) {
            this.save();
        }
    }

    public File newOptions;
    public FileConfiguration s;

    public void configPlayerData() {
        this.newOptions = new File("plugins/DeathBan", "playerdata.yml");
        this.s = (FileConfiguration) YamlConfiguration.loadConfiguration(this.newOptions);
        savePlayerData();
    }
    public void savePlayerData(){
        try {
            this.s.save(this.newOptions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.s.save(this.newOptions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        YamlConfiguration.loadConfiguration(this.newOptions);
    }

}
