package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.main.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.WeaponManager;
import be4rjp.sclat2.weapon.main.runnable.ChargerRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChargeShootListener implements Listener {
    
    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = player.getInventory().getItemInMainHand();
        event.setCancelled(true);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                SclatWeapon sclatWeapon = WeaponManager.getSclatWeaponByItem(bow);
                if(sclatWeapon == null) return;
                if(!(sclatWeapon instanceof MainWeapon)) return;
                
                SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                ChargerRunnable runnable = (ChargerRunnable) sclatPlayer.getMainWeaponTaskMap().get(sclatWeapon);
                if(runnable == null) return;
                runnable.shoot();
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
}
