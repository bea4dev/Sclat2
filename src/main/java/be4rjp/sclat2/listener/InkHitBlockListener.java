package be4rjp.sclat2.listener;

import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.event.AsyncInkHitBlockEvent;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.MainWeapon;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InkHitBlockListener implements Listener {
    
    private static SclatSound INK_HIT_SOUND = new SclatSound(Sound.ENTITY_SLIME_ATTACK, 0.25F, 2.0F);
    
    @EventHandler
    public void onInkHit(AsyncInkHitBlockEvent event){
        InkBullet inkBullet = event.getInkBullet();
        SclatPlayer shooter = inkBullet.getShooter();
        if(shooter == null) return;
        if(shooter.getSclatTeam() == null) return;
    
        SclatTeam team = shooter.getSclatTeam();
        Location center = event.getHitLocation();
        MainWeapon mainWeapon = inkBullet.getMainWeapon();
        team.getMatch().paint(shooter, center, mainWeapon.getPaintRadius());
        team.getMatch().playSound(INK_HIT_SOUND, center);
    }
}
