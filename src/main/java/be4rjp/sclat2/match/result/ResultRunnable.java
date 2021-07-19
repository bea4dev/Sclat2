package be4rjp.sclat2.match.result;

import be4rjp.cinema4c.data.play.MovieData;
import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.SclatEntity;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.player.ObservableOption;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

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
    
        //ムービー再生
        SclatMap sclatMap = match.getSclatMap();
        MovieData movieData = sclatMap.getResultMovie();
        if(movieData != null) {
            Set<Player> players = new HashSet<>();
            match.getPlayers().stream()
                    .filter(sclatPlayer -> sclatPlayer.getBukkitPlayer() != null)
                    .forEach(sclatPlayer -> players.add(sclatPlayer.getBukkitPlayer()));
    
            match.setPlayerObservableOption(ObservableOption.ALONE);
            movieData.play(players);
        }
        
        new NawabariResultRunnable(match).start();
        this.cancel();
    }
    
    public void start(){this.runTaskTimerAsynchronously(Sclat.getPlugin(), 60, 10);}
}
