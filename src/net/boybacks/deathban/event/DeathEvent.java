package net.boybacks.deathban.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class DeathEvent implements Listener {

    //to do config.yml
    int time = 30;
    String mark = "m";
    String reason = "Zginąłeś! Czekaj aż się odrodzisz!";

    //to zostawić nie tykać!
    String[] cmd = {"tempban ", " "};

    @EventHandler
    public void Death(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        if(e.getEntity() instanceof Player) {
            if (!(player.hasPermission("DeathBan.Death"))){
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd[0] + player.getName() + cmd[1] + time + mark + cmd[1] + reason);
            }
        }
    }
}