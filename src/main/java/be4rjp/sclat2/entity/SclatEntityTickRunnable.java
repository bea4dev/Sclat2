package be4rjp.sclat2.entity;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import org.bukkit.scheduler.BukkitRunnable;

public class SclatEntityTickRunnable extends BukkitRunnable {
    
    private final Match match;
    
    public SclatEntityTickRunnable(Match match){
        this.match = match;
    }
    
    @Override
    public void run() {
        match.getSclatEntities().forEach(SclatEntity::tick);
    }
    
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
    }
}
