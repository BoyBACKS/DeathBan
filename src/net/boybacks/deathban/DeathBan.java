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
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.UUID;
public class DeathBan extends JavaPlugin implements Listener{

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(fix("\n\n &7| &6Death&cBan &7| &aPlugin is on\n\n"));
        getServer().getPluginManager().registerEvents(this, this);
        config();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(fix("\n\n &7| &6Death&cBan &7| &aPlugin is &coff\n\n"));
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
        int death = this.s.getInt(uuid + ".deathcount");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!(player.hasPermission("deathban.bypass"))) {
            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                            getConfig().getString(fix("command")).replace("{PLAYER}", player.getName())); }}, 5L);
        } else if (player.hasPermission("deathban.bypass")) {
            player.sendMessage(fix(this.getConfig().getString("message")));
        }
        this.s.set(uuid + ".deathcount", death + 1);
        this.save();
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

    public static String fix(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}