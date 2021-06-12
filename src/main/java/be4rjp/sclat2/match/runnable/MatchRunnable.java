package be4rjp.sclat2.match.runnable;

import be4rjp.sclat2.match.Match;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class MatchRunnable extends BukkitRunnable {
    
    protected final Match match;
    protected int timeLeft = 0;
    
    /**
     * 試合のスケジューラーを作成します。
     * @param match 試合のインスタンス
     * @param timeLimit 試合の最大時間
     */
    public MatchRunnable(Match match, int timeLimit){
        this.match = match;
        this.timeLeft = timeLimit;
    }
    
    @Override
    public abstract void run();
}
