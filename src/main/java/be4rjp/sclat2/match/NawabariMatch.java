package be4rjp.sclat2.match;

import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.NawabariMatchRunnable;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.PlayerSquidRunnable;
import be4rjp.sclat2.player.SclatPlayer;

import java.util.HashSet;
import java.util.Set;

public class NawabariMatch extends Match{
    
    public NawabariMatch(SclatMap sclatMap) {
        super(sclatMap);
    }
    
    @Override
    public MatchType getType() {
        return MatchType.NAWABARI;
    }
    
    @Override
    public void initializePlayer(SclatPlayer sclatPlayer) {
        PlayerSquidRunnable squidRunnable = new PlayerSquidRunnable(sclatPlayer);
        squidRunnable.start();
        squidRunnableSet.add(squidRunnable);
    }
    
    @Override
    public void end() {
    
    }
    
    
    @Override
    public void initialize() {
        this.matchRunnable = new NawabariMatchRunnable(this, 180);
    }
    
    @Override
    public boolean checkWin() {
        return false;
    }
    
    @Override
    public SclatTeam getWinner() {
        int paint = 0;
        Set<SclatTeam> winTeam = new HashSet<>();
        for(SclatTeam sclatTeam : this.getSclatTeams()){
            int teamPaint = sclatTeam.getPaints();
            if(paint <= teamPaint){
                if(paint != teamPaint) winTeam.clear();
                winTeam.add(sclatTeam);
                paint = teamPaint;
            }
        }
        
        if(winTeam.size() == 1){
            for(SclatTeam team : winTeam) return team;
        }else{
            return null;
        }
        return null;
    }
}
