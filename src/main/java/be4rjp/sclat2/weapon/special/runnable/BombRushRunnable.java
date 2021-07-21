package be4rjp.sclat2.weapon.special.runnable;

import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class BombRushRunnable extends BukkitRunnable {

    private final SclatPlayer sclatPlayer;

    public BombRushRunnable(SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;
    }

    @Override
    public void run() {
        sclatPlayer.setUsingBombRush(false);
    }
}
