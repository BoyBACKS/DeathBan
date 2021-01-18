package net.boybacks.deathban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    //Do usunięcia
    int min = this.getConfig().getInt("min");
    int max = this.getConfig().getInt("max");


    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.GREEN + " Plugin DeathBan jest aktywny\n\n");
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[DeathBan]" + ChatColor.RED + " Plugin DeathBan jest nieaktywny\n\n");
    }
    //to do data.yml
//    int deathcount = ;

    //to do config.yml
    Object random = this.getConfig().getString("random");
    int time = this.getConfig().getInt("time");
    String mark = this.getConfig().getString("mark");
    String reason = this.getConfig().getString("reason");

    //to zostawić nie tykać!
    String[] cmd = {"tempban ", " "};

    @EventHandler
    public void Death(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        if(e.getEntity() instanceof Player) {
            if (!(player.hasPermission("DeathBan.Death"))) {
                if (random == "false") {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd[0] + player.getName() + cmd[1] + time + mark + cmd[1] + reason);
                }
                else if (random == "true") {
                    int RandomNo = (int)(Math.random()*(max-min+1)+min);
                    int RandomTime =  time * RandomNo;
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd[0] + player.getName() + cmd[1] + RandomTime + mark + cmd[1] + reason);
                }
            }
        }
    }
}