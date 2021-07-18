package be4rjp.sclat2.match.result;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.SclatEntity;
import be4rjp.sclat2.match.Match;
import org.bukkit.scheduler.BukkitRunnable;

public class ResultRunnable extends BukkitRunnable {
    
    private final Match match;
    
    public ResultRunnable(Match match){
        this.match = match;
    }
    
    @Override
    public void run() {
        for(SclatEntity sclatEntity : match.getSclatEntities()){
            if(!sclatEntity.isDead()) return;
        }
        
        new NawabariResultRunnable(match).start();
        this.cancel();
    }
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 10);
    }
}
