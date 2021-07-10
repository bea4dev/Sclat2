package be4rjp.sclat2.match.intro;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.ObservableOption;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatSound;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class ReadyRunnable extends BukkitRunnable {
    
    private static final SclatSound GO_SOUND = new SclatSound(Sound.ENTITY_ZOMBIE_INFECT, 10.0F, 2.0F);

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
        
        if(count > 10 && count < 17){
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("match-ready-" + (count - 10), null, 0, 10, 0));
        }
        
        if(count == 30){
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("match-ready-go", null, 2, 7, 2));
            match.start();
            match.playSound(GO_SOUND);
            match.getPlayers().forEach(SclatPlayer::equipWeaponClass);
            this.cancel();
        }
        
        count++;
    }


    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
    
    
    @Override
    public synchronized void cancel() throws IllegalStateException {
        match.setPlayerObservableOption(ObservableOption.ONLY_MATCH_PLAYER);
        
        super.cancel();
    }
}
