package be4rjp.sclat2.listener;

import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.event.AsyncInkHitPlayerEvent;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InkHitPlayerListener implements Listener {
    
    @EventHandler
    public void onHitPlayer(AsyncInkHitPlayerEvent event){
        InkBullet inkBullet = event.getInkBullet();
        SclatPlayer hitPlayer = event.getHitPlayer();
        hitPlayer.giveDamage(inkBullet.getMainWeapon().getDamage(), inkBullet.getShooter());
    }
}
