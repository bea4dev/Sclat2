package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class MainWeaponRunnable extends BukkitRunnable {
    
    protected final SclatPlayer sclatPlayer;
    protected final MainWeapon mainWeapon;
    
    public MainWeaponRunnable(MainWeapon mainWeapon, SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;
        this.mainWeapon = mainWeapon;
        
        sclatPlayer.getMainWeaponTaskMap().put(mainWeapon, this);
    }
    
    @Override
    public abstract void run();
}
