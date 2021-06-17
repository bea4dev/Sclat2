package be4rjp.sclat2.match.intro;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * マップの名前とゲームモードの表示
 */
public class MapIntroRunnable extends BukkitRunnable {
    
    private final Match match;
    
    public MapIntroRunnable(Match match){
        this.match = match;
    }
    
    @Override
    public void run() {
        match.getPlayers().forEach(sclatPlayer ->
                sclatPlayer.sendTextTitle("match-" + match.getType(), "match-" + match.getType() + "-des", 20, 100, 20));
    }
    
    
    public void start(){
        this.runTaskLaterAsynchronously(Sclat.getPlugin(), 5);
    }
}
