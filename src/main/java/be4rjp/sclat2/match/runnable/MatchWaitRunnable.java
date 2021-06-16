package be4rjp.sclat2.match.runnable;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.intro.IntroManager;
import be4rjp.sclat2.message.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatScoreboard;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MatchWaitRunnable extends BukkitRunnable {

    //デフォルトのプレイヤー待機時間[sec]
    private static final int DEFAULT_WAIT_TIME = 20;
    //試合を開始するのに必要な最低人数
    private static final int START_MIN_PLAYER = 1;

    private final Match match;
    private int timeLeft = DEFAULT_WAIT_TIME;

    public MatchWaitRunnable(Match match){this.match = match;}


    @Override
    public void run() {

        if(match.getPlayers().size() < START_MIN_PLAYER){
            timeLeft = DEFAULT_WAIT_TIME;
        }

        //スコアボード
        SclatScoreboard scoreboard = match.getScoreboard();
        for(SclatPlayer sclatPlayer : match.getPlayers()) {
            Lang lang = sclatPlayer.getLang();
            List<String> lines = new ArrayList<>();
            lines.add("");
            lines.add("§a" + MessageManager.getText(lang, "match-map") + " » §r§l" + match.getSclatMap().getDisplayName(lang));
            lines.add(" ");
            lines.add("§a" + MessageManager.getText(lang, "match-mode") + " » §6§l" + match.getType().getDisplayName(lang));
            lines.add("   ");
            if(match.getPlayers().size() < START_MIN_PLAYER)
                lines.add("§b" + String.format(MessageManager.getText(lang, "match-wait-player"), START_MIN_PLAYER - match.getPlayers().size()));
            else
                lines.add("§b" + MessageManager.getText(lang, "match-wait-time") + " » §r§l" + timeLeft + MessageManager.getText(lang, "word-sec"));
            scoreboard.setSidebarLine(sclatPlayer, lines);
        }
        scoreboard.updateSidebar(match.getPlayers());

        if(timeLeft == 0){
            //開始処理
            IntroManager.playIntro(match);
            this.cancel();
        }
        timeLeft--;
    }
    
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 20);
    }
}
