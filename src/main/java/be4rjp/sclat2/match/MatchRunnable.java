package be4rjp.sclat2.match;

import org.bukkit.scheduler.BukkitRunnable;

public class MatchRunnable extends BukkitRunnable {
    
    private final Match match;
    private int sec = 0;
    
    /**
     * 試合のスケジューラーを作成します。
     * @param match 試合のインスタンス
     * @param timeLimit 試合の最大時間
     */
    public MatchRunnable(Match match, int timeLimit){
        this.match = match;
        this.sec = timeLimit;
    }
    
    @Override
    public void run() {
    
    }
}
