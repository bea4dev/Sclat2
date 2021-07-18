package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.SclatConfig;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.death.DeathType;
import be4rjp.sclat2.player.death.PlayerDeathManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CancelListener implements Listener {
    
    @EventHandler
    public void onDamageByFall(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        
        event.setCancelled(true);
    
        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            Player player = (Player) event.getEntity();
    
            new BukkitRunnable() {
                @Override
                public void run() {
                    SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                    SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
                    
                    boolean lobbyPlayer = false;
                    if(sclatTeam == null) lobbyPlayer = true;
                    if(sclatTeam == Sclat.getLobbyTeam()) lobbyPlayer = true;
                    
                    if(lobbyPlayer){
                        sclatPlayer.teleport(SclatConfig.getLobbyLocation());
                        return;
                    }
    
                    if(!sclatPlayer.isDeath()) PlayerDeathManager.death(sclatPlayer, sclatPlayer, null, DeathType.FELL_OUT_OF_THE_WORLD);
                }
            }.runTaskAsynchronously(Sclat.getPlugin());
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }
}
