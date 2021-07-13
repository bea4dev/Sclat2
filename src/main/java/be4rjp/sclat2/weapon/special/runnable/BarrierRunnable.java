package be4rjp.sclat2.weapon.special.runnable;

import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class BarrierRunnable extends BukkitRunnable {
    
    private final SclatPlayer sclatPlayer;
    int tick = 0;
    
    public BarrierRunnable(SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;
    }
    
    @Override
    public void run() {
        sclatPlayer.setBarrier(false);
    }
}
