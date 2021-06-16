package be4rjp.sclat2.match.intro;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReadyRunnable extends BukkitRunnable {

    private final SclatMap sclatMap;
    private final Match match;

    private int count = 0;

    public ReadyRunnable(Match match){
        this.sclatMap = match.getSclatMap();
        this.match = match;
    
        match.getPlayers().forEach(match::teleportToTeamLocation);
        match.getPlayers().forEach(sclatPlayer -> sclatPlayer.setGameMode(GameMode.ADVENTURE));
    }

    @Override
    public void run() {
        match.getPlayers().forEach(match::teleportToTeamLocation);
        
        if(count > 5 && count < 12){
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("match-ready-" + (count - 5), null, 0, 10, 0));
        }
        
        if(count == 20){
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("match-ready-go", null, 2, 7, 2));
            match.start();
            this.cancel();
        }
        
        count++;
    }


    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
}
