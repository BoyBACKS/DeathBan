package net.boybacks.deathban;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class DeathBan extends JavaPlugin implements Listener{

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(fix("\n\n &7| &6Death&cBan &7| &aPlugin is on\n\n"));
        getServer().getPluginManager().registerEvents(this, this);
        config();
        update();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(fix("\n\n &7| &6Death&cBan &7| &aPlugin is &coff\n\n"));
        this.saveDefaultConfig();
    }

    public boolean onCommand(CommandSender S, Command c, String s, String[] args) {
        if (S instanceof Player) {
            Player player = (Player) S;
            if (c.getName().equalsIgnoreCase("deathban")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("reset") && player.hasPermission("deathban.reset")) {
                    UUID uuid = player.getUniqueId();
                    this.s.set(uuid + ".deathscore", 0);
                    player.sendMessage(fix(this.getConfig().getString("messages.reset")));
                    this.save();
                } else if (args.length == 1 && args[0].equalsIgnoreCase("help") && player.hasPermission("deathban.help")) {
                    player.sendMessage(fix(this.getConfig().getString("messages.help")));
                } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle") && player.hasPermission("deathban.toggle")) {
                    boolean toggle = this.getConfig().getBoolean("enable");
                    if (toggle == true) {
                        this.getConfig().set("enable", false);
                        player.sendMessage(fix(this.getConfig().getString("messages.disable")));
                    } else {
                        this.getConfig().set("enable", true);
                        player.sendMessage(fix(this.getConfig().getString("messages.enable")));
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("reset") && player.hasPermission("deathban.reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(fix(this.getConfig().getString("messages.no_player").replace("{PLAYER}", args[1])));
                    } else {
                        UUID uuidt = target.getUniqueId();
                        this.s.set(uuidt + ".deathscore", 0);
                        player.sendMessage(fix(this.getConfig().getString("messages.resetother").replace("{PLAYER}", target.getName())));
                        this.save();
                    }
                } else if (args.length == 1 && player.hasPermission("deathban.scoreother")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(fix(this.getConfig().getString("messages.no_player").replace("{PLAYER}", args[0])));
                    } else {
                        UUID uuidt = target.getUniqueId();
                        String score = String.valueOf(this.s.getInt(uuidt + ".deathscore"));
                        player.sendMessage(fix(this.getConfig().getString("messages.scoreother").replace("{SCORE}", score)
                                .replace("{PLAYER}",  target.getName())));
                    }
                } else if (player.hasPermission("deathban.score")) {
                    UUID uuid = player.getUniqueId();
                    String score = String.valueOf(this.s.getInt(uuid + ".deathscore"));
                    player.sendMessage(fix(this.getConfig().getString("messages.score").replace("{SCORE}", score)));
                } else {
                    player.sendMessage(fix(this.getConfig().getString("messages.no_permission")));
                }
            }
        } else {
            S.sendMessage(fix(this.getConfig().getString("messages.console")));
        }
        return true;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        UUID uuid = player.getUniqueId();
        int death = this.s.getInt(uuid + ".deathscore");
        int time = this.getConfig().getInt("time");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (this.getConfig().getBoolean("enable")) {
            if (!(player.hasPermission("deathban.bypass"))) {
                scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                                getConfig().getString(fix("command")).replace("{PLAYER}", player.getName()));
                    }
                }, time);
            } else if (player.hasPermission("deathban.bypass")) {
                player.sendMessage(fix(this.getConfig().getString("messages.bypass_die")));
            }
        }
        this.s.set(uuid + ".deathscore", death + 1);
        this.save();
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

    public void config() {
        this.saveDefaultConfig();
        this.configPlayerData();
        this.save();
    }

    public void update() {
        Logger logger = this.getLogger();

        new UpdateChecker(this, 91723).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info(fix("&aThere is not a new update available."));
            } else {
                logger.info(fix("&aThere is a new update available."));
            }
        });
    }

    public static String fix(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}