package be4rjp.sclat2.match.runnable;

import be4rjp.sclat2.match.Match;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PVPMatchRunnable extends MatchRunnable{
    
    
    
    /**
     * 試合のスケジューラーを作成します。
     *
     * @param match     試合のインスタンス
     * @param timeLimit 試合の最大時間
     */
    public PVPMatchRunnable(Match match, int timeLimit) {
        super(match, timeLimit);
        
    }
    
    @Override
    public void run() {
    
    }
}
