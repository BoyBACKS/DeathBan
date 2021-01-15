package net.boybacks.deathban.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler
    public void Death(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        if(e.getEntity() instanceof Player) {
            if (!(player.hasPermission("DeathBan.Death"))){
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tempban " + player.getName() + " 10s Zginąłeś! Czekaj aż się odrodzisz!");
            }
        }
    }
}