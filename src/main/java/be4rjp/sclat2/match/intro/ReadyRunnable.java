package be4rjp.sclat2.match.intro;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class ReadyRunnable extends BukkitRunnable {

    private final SclatMap sclatMap;
    private final Match match;

    private int count = 0;

    public ReadyRunnable(SclatMap sclatMap, Match match){
        this.sclatMap = sclatMap;
        this.match = match;

        for(SclatPlayer sclatPlayer : match.getPlayers()){
            SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
            if(sclatTeam == null) continue;

            int index = match.getSclatTeams().indexOf(sclatTeam);
            sclatPlayer.teleportAsync(match.getSclatMap().getTeamLocation(index));
        }
    }

    @Override
    public void run() {

    }


    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
}
