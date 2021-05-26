package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.event.AsyncUseMainWeaponEvent;
import be4rjp.sclat2.event.AsyncUseWeaponEvent;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.WeaponManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerItemClickListener implements Listener {
    @EventHandler
    public void onClickItem(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!event.hasItem()) return;
        if(event.getHand() != EquipmentSlot.HAND && event.getHand() != EquipmentSlot.OFF_HAND) return;
    
        new BukkitRunnable() {
            @Override
            public void run() {
                SclatWeapon sclatWeapon = WeaponManager.getSclatWeaponByItem(event.getItem());
                SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                if(sclatWeapon == null) return;
                AsyncUseWeaponEvent weaponEvent = new AsyncUseWeaponEvent(sclatPlayer, sclatWeapon, event.getAction());
                Sclat.getPlugin().getServer().getPluginManager().callEvent(weaponEvent);
    
                MainWeapon mainWeapon = WeaponManager.getMainWeaponByItem(event.getItem());
                if(mainWeapon == null) return;
                AsyncUseMainWeaponEvent mainWeaponEvent = new AsyncUseMainWeaponEvent(sclatPlayer, mainWeapon, event.getAction());
                Sclat.getPlugin().getServer().getPluginManager().callEvent(mainWeaponEvent);
    
                Action action = event.getAction();
                if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
                    mainWeapon.onLeftClick(sclatPlayer);
                if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                    mainWeapon.onRightClick(sclatPlayer);
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
}
