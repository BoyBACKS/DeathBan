package net.boybacks.deathban;

import net.boybacks.deathban.helpers.UpdateChecker;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import net.boybacks.deathban.helpers.*;
import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class DeathBan extends JavaPlugin implements Listener {

    public String version = "1.7";

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
                        UUID uuid = target.getUniqueId();
                        this.s.set(uuid + ".deathscore", 0);
                        player.sendMessage(fix(this.getConfig().getString("messages.resetother").replace("{PLAYER}", target.getName())));
                        this.save();
                    }
                } else if (args.length == 1 && player.hasPermission("deathban.scoreother")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage(fix(this.getConfig().getString("messages.no_player").replace("{PLAYER}", args[0])));
                    } else {
                        if (player.hasPermission("deathban.guiother") && this.getConfig().getBoolean("gui.enable") == true) {
                            this.guitarget(player, target);
                            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 0.7f);
                        }
                        else {
                            UUID uuid = target.getUniqueId();
                            String score = String.valueOf(this.s.getInt(uuid + ".deathscore"));
                            player.sendMessage(fix(this.getConfig().getString("messages.scoreother").replace("{SCORE}", score)
                                    .replace("{PLAYER}", target.getName())));
                        }
                    }
                } else if (player.hasPermission("deathban.score")) {
                    UUID uuid = player.getUniqueId();
                    String score = String.valueOf(this.s.getInt(uuid + ".deathscore"));
                    if (player.hasPermission("deathban.admin")) {
                        this.admingui(player);
                        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 0.7f);
                    } else if (this.getConfig().getBoolean("gui.enable") == true) {
                        this.gui(player);
                        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 0.7f);
                    } else {
                        player.sendMessage(fix(this.getConfig().getString("messages.score").replace("{SCORE}", score)));
                    }
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
        Location loc = player.getLocation();
        int death = this.s.getInt(uuid + ".deathscore");
        int time = this.getConfig().getInt("time");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.s.set(uuid + ".nickname", (Object) player.getName());
        this.s.set(uuid + ".lastdeathlocation.X", (Object)loc.getBlockX());
        this.s.set(uuid + ".lastdeathlocation.Y", (Object)loc.getBlockY());
        this.s.set(uuid + ".lastdeathlocation.Z", (Object)loc.getBlockZ());
        this.s.set(uuid + ".lastdeathlocation.World", (Object)loc.getWorld().getName().toString());
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

    public void gui(Player player) {
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory((InventoryHolder) player, 36, "DeathBanGui");

        if (this.getConfig().getBoolean("gui.nickname") == true) {
            ItemStack item = new ItemBuilder(Material.NAME_TAG).setTitle("&b" + player.getName()).toItemStack();
            inventory.setItem(13, item);
        }
        if (this.getConfig().getBoolean("gui.deathcouter") == true) {
            ItemStack item2 = new ItemBuilder(Material.KNOWLEDGE_BOOK).setTitle("&bDeathCouter")
                    .addLoreLine(fix("&bCount: &f" + this.s.getString(uuid + ".deathscore"))).toItemStack();
            inventory.setItem(20, item2);
        }
        if (this.getConfig().getBoolean("gui.lastdeathlocation") == true) {
            ItemStack item3 = new ItemBuilder(Material.COMPASS).setTitle("&bLast Death Location")
                    .addLoreLine(fix("&bX: &f" + this.s.getString(uuid + ".lastdeathlocation.X")))
                    .addLoreLine(fix("&bY: &f" + this.s.getString(uuid + ".lastdeathlocation.Y")))
                    .addLoreLine(fix("&bZ: &f" + this.s.getString(uuid + ".lastdeathlocation.Z")))
                    .addLoreLine(fix("&bWorld: &f" + this.s.getString(uuid + ".lastdeathlocation.World"))).toItemStack();
            inventory.setItem(24, item3);
        }
        
        ItemStack item4 = new ItemBuilder(Material.BARRIER).setTitle("&cExit").toItemStack();
        inventory.setItem(31, item4);
        
        player.openInventory(inventory);
    }

    public void guitarget(Player player, Player target) {
        UUID uuid = target.getUniqueId();
        Inventory inventory = Bukkit.createInventory((InventoryHolder) target, 36, "DeathBanGui");

        if (this.getConfig().getBoolean("gui.nickname") == true) {
            ItemStack item = new ItemBuilder(Material.NAME_TAG).setTitle("&b" + target.getName()).toItemStack();
            inventory.setItem(13, item);
        }
        if (this.getConfig().getBoolean("gui.deathcouter") == true) {
            ItemStack item2 = new ItemBuilder(Material.KNOWLEDGE_BOOK).setTitle("&bDeathCouter")
                    .addLoreLine(fix("&bCount: &f" + this.s.getString(uuid + ".deathscore"))).toItemStack();
            inventory.setItem(20, item2);
        }
        if (this.getConfig().getBoolean("gui.lastdeathlocation") == true) {
            ItemStack item3 = new ItemBuilder(Material.COMPASS).setTitle("&bLast Death Location")
                    .addLoreLine(fix("&bX: &f" + this.s.getString(uuid + ".lastdeathlocation.X")))
                    .addLoreLine(fix("&bY: &f" + this.s.getString(uuid + ".lastdeathlocation.Y")))
                    .addLoreLine(fix("&bZ: &f" + this.s.getString(uuid + ".lastdeathlocation.Z")))
                    .addLoreLine(fix("&bWorld: &f" + this.s.getString(uuid + ".lastdeathlocation.World"))).toItemStack();
            inventory.setItem(24, item3);
        }

        ItemStack item4 = new ItemBuilder(Material.BARRIER).setTitle("&cExit").toItemStack();
        inventory.setItem(31, item4);

        player.openInventory(inventory);
    }

    public void admingui(Player player) {
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory((InventoryHolder) player, 36, "AdminDeathBanGui");

        if (this.getConfig().getBoolean("gui.enable") == true) {
            ItemStack on = new ItemBuilder(Material.EMERALD_BLOCK).setTitle("&bPlayer Gui is: &a&lOn").toItemStack();
            inventory.setItem(4, on);
        } else {
            ItemStack off = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle("&bPlayer Gui is: &c&lOff").toItemStack();
            inventory.setItem(4, off);
        }

        if (this.getConfig().getBoolean("enable") == true) {
            ItemStack on = new ItemBuilder(Material.ENDER_EYE).setTitle("&bDeath event is: &a&lOn").toItemStack();
            inventory.setItem(8, on);
        } else {
            ItemStack off = new ItemBuilder(Material.ENDER_PEARL).setTitle("&bDeath event is: &c&lOff").toItemStack();
            inventory.setItem(8, off);
        }

        ItemStack item = new ItemBuilder(Material.NAME_TAG).setTitle("&b" + player.getName()).toItemStack();
        inventory.setItem(13, item);

        if (this.getConfig().getBoolean("gui.nickname") == true) {
            ItemStack on = new ItemBuilder(Material.LIME_DYE).setTitle("&bNickName slot is: &a&lOn").toItemStack();
            inventory.setItem(22, on);
        } else {
            ItemStack off = new ItemBuilder(Material.GRAY_DYE).setTitle("&bNickName slot is: &c&lOff").toItemStack();
            inventory.setItem(22, off);
        }

        ItemStack item2 = new ItemBuilder(Material.KNOWLEDGE_BOOK).setTitle("&bDeathCouter")
                .addLoreLine(fix("&bCount: &f" + this.s.getString(uuid +".deathscore"))).toItemStack();
        inventory.setItem(20, item2);

        if (this.getConfig().getBoolean("gui.deathcouter") == true) {
            ItemStack on = new ItemBuilder(Material.LIME_DYE).setTitle("&bDeath couter slot is: &a&lOn").toItemStack();
            inventory.setItem(29, on);
        } else {
            ItemStack off = new ItemBuilder(Material.GRAY_DYE).setTitle("&bDeath couter slot is: &c&lOff").toItemStack();
            inventory.setItem(29, off);
        }

        ItemStack item3 = new ItemBuilder(Material.COMPASS).setTitle("&bLast Death Location")
                .addLoreLine(fix("&bX: &f" + this.s.getString(uuid + ".lastdeathlocation.X")))
                .addLoreLine(fix("&bY: &f" + this.s.getString(uuid + ".lastdeathlocation.Y")))
                .addLoreLine(fix("&bZ: &f" + this.s.getString(uuid + ".lastdeathlocation.Z")))
                .addLoreLine(fix("&bWorld: &f" + this.s.getString(uuid + ".lastdeathlocation.World"))).toItemStack();
        inventory.setItem(24, item3);

        if (this.getConfig().getBoolean("gui.lastdeathlocation") == true) {
            ItemStack on = new ItemBuilder(Material.LIME_DYE).setTitle("&bLast death location slot is: &a&lOn").toItemStack();
            inventory.setItem(33, on);
        } else {
            ItemStack off = new ItemBuilder(Material.GRAY_DYE).setTitle("&bLast death location slot is: &c&lOff").toItemStack();
            inventory.setItem(33, off);
        }

        ItemStack item4 = new ItemBuilder(Material.BARRIER).setTitle("&cExit").toItemStack();
        inventory.setItem(31, item4);

        ItemStack item5 = new ItemBuilder(Material.LEGACY_BOOK_AND_QUILL).setTitle("&bVersion: &f" + version).addGlow()
                .addLoreLine(fix("&bAuthor: &fBoyBACKS"))
                .addLoreLine(fix("&b&m-----------"))
                .addLoreLine(fix("&b[ &fClick Here &b]"))
                .addLoreLine(fix("&b&m-----------")).toItemStack();
        inventory.setItem(35, item5);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase(fix("DeathBanGui"))) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5f, 0.7f);
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(fix("AdminDeathBanGui"))) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }

            if (e.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5f, 0.7f);
            }

            if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                this.getConfig().set("gui.enable", false);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.5f);
                this.saveConfig();
                this.admingui(player);
            }
            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                this.getConfig().set("gui.enable", true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.7f);
                this.saveConfig();
                this.admingui(player);
            }

            if (e.getCurrentItem().getType() == Material.ENDER_EYE) {
                this.getConfig().set("enable", false);
                player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 3.5f, 1.0f);
                this.saveConfig();
                this.admingui(player);
            }
            if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
                this.getConfig().set("enable", true);
                player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 3.5f, 0.7f);
                this.saveConfig();
                this.admingui(player);
            }

            if (e.getSlot() == 22) {
                if (this.getConfig().getBoolean("gui.nickname") == true) {
                    this.getConfig().set("gui.nickname", false);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.5f);
                } else {
                    this.getConfig().set("gui.nickname", true);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.7f);
                }
                this.saveConfig();
                this.admingui(player);
            }

            if (e.getSlot() == 29) {
                if (this.getConfig().getBoolean("gui.deathcouter") == true) {
                    this.getConfig().set("gui.deathcouter", false);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.5f);
                } else {
                    this.getConfig().set("gui.deathcouter", true);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.7f);
                }
                this.saveConfig();
                this.admingui(player);
            }

            if (e.getSlot() == 33) {
                if (this.getConfig().getBoolean("gui.lastdeathlocation") == true) {
                    this.getConfig().set("gui.lastdeathlocation", false);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.5f);
                } else {
                    this.getConfig().set("gui.lastdeathlocation", true);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.5f, 0.7f);
                }
                this.saveConfig();
                this.admingui(player);
            }

            if (e.getSlot() == 35) {
                player.sendMessage(fix("&bFor more informations see my page: " +
                        "\n • &fSpigot:&b https://www.spigotmc.org/members/boybacks.737508/" +
                        "\n • &fGithub:&b https://github.com/BoyBACKS/"));
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 3.5f, 0.7f);
            }
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

    public void config() {
        this.saveDefaultConfig();
        this.configPlayerData();
        this.save();
    }

    public void update() {
        Logger logger = this.getLogger();

        new UpdateChecker(this, 91723).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info(fix(this.getConfig().getString("messages.update")));
            } else {
                logger.info(fix(this.getConfig().getString("messages.noupdate")));
            }
        });
    }

    public static String fix(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}