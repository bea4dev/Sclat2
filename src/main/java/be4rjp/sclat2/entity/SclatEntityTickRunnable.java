package be4rjp.sclat2.entity;

import org.bukkit.scheduler.BukkitRunnable;

public class SclatEntityTickRunnable extends BukkitRunnable {
    
    private final SclatEntity sclatEntity;
    
    public SclatEntityTickRunnable(SclatEntity sclatEntity){
        this.sclatEntity = sclatEntity;
    }
    
    @Override
    public void run() {
        sclatEntity.tick();
    }
}
