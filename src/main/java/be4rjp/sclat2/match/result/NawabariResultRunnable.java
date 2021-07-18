package be4rjp.sclat2.match.result;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.util.ProgressBar;
import be4rjp.sclat2.util.SclatSound;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class NawabariResultRunnable extends BukkitRunnable {
    
    private static final SclatSound SHOW_RESULT_SOUND = new SclatSound(Sound.ENTITY_ZOMBIE_INFECT, 13.0F, 1.5F);
    private static final SclatSound WIN_SOUND = new SclatSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    
    private final Match match;
    private final SclatTeam winTeam;
    
    private int tick = 0;
    private int g = 0;
    
    public NawabariResultRunnable(Match match){
        this.match = match;
        
        this.winTeam = match.getWinner();
    }
    
    @Override
    public void run() {
    
        if(tick <= 15) {
            ProgressBar left = new ProgressBar(50).setProgressPercent(g * 2).setFrame(false);
            ProgressBar right = new ProgressBar(50).setProgressPercent(100 - g * 2).setEmptyColor(match.getSclatTeams().get(1).getSclatColor().getChatColor().toString()).setFrame(false);
            
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("none", new Object[]{}, "none-s", new Object[]{
                    g + "% [" + left.toString(match.getSclatTeams().get(0).getSclatColor().getChatColor().toString()) + right.toString("§7") + "§7]§r " + g + "%"
            }, 0, 40, 0));
            
            g += 2;
        }
        
        
        if(tick == 35){
            String team0Color = match.getSclatTeams().get(0).getSclatColor().getChatColor().toString();
            String team1Color = match.getSclatTeams().get(1).getSclatColor().getChatColor().toString();
            
            match.playSound(SHOW_RESULT_SOUND);
            
            if(winTeam == null){
                match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendTextTitle("none", new Object[]{}, "none-s", new Object[]{
                        "50% " + new ProgressBar(100).setProgress(50).setEmptyColor(team1Color).toString(team0Color) + " 50%"
                }, 0, 40, 0));
            }else{
                if(winTeam == match.getSclatTeams().get(0)){
                    int winTeamPaint = winTeam.getPaints();
                    int loseTeamPaint = match.getSclatTeams().get(1).getPaints();
                    
                    int maxPaint = winTeamPaint + loseTeamPaint;
                    double percent = (double) winTeamPaint / (double) maxPaint * 100.0;
    
                    match.getPlayers().stream().filter(sclatPlayer -> sclatPlayer.getSclatTeam() != null)
                            .forEach(sclatPlayer -> sclatPlayer.sendTextTitle(sclatPlayer.getSclatTeam() == winTeam ? "match-result-win" : "match-result-lose", new Object[]{}, "none-s", new Object[]{
                            (int)percent + "% " + new ProgressBar(100).setProgressPercent(percent).setEmptyColor(team1Color).toString(team0Color) + " " + (100 - (int)percent) + "%"
                    }, 0, 40, 0));
                }else{
                    int winTeamPaint = winTeam.getPaints();
                    int loseTeamPaint = match.getSclatTeams().get(0).getPaints();
    
                    int maxPaint = winTeamPaint + loseTeamPaint;
                    double percent = (double) winTeamPaint / (double) maxPaint * 100.0;
    
                    match.getPlayers().stream().filter(sclatPlayer -> sclatPlayer.getSclatTeam() != null)
                            .forEach(sclatPlayer -> sclatPlayer.sendTextTitle(sclatPlayer.getSclatTeam() == winTeam ? "match-result-win" : "match-result-lose", new Object[]{}, "none-s", new Object[]{
                            (100 - (int)percent) + "% " + new ProgressBar(100).setProgressPercent(100 - percent).setEmptyColor(team1Color).toString(team0Color) + " " + (int)percent + "%"
                    }, 0, 40, 0));
                }
            }
        }
    
        if(tick == 40){
            match.getPlayers().stream().filter(sclatPlayer -> sclatPlayer.getSclatTeam() != null)
                    .filter(sclatPlayer -> sclatPlayer.getSclatTeam() == winTeam)
                    .forEach(sclatPlayer -> sclatPlayer.playSound(WIN_SOUND));
            match.end();
            cancel();
        }
        
        
        tick++;
    }
    
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
}
