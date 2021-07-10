package be4rjp.sclat2.match.runnable;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.MatchManager;
import be4rjp.sclat2.match.intro.IntroManager;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatScoreboard;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MatchWaitRunnable extends BukkitRunnable {

    //デフォルトのプレイヤー待機時間[sec]
    private static final int DEFAULT_WAIT_TIME = 20;
    
    //試合を開始するのに必要な最低人数
    private final int minPlayer;

    private final MatchManager matchManager;
    private final Match match;
    private int timeLeft = DEFAULT_WAIT_TIME;
    
    public MatchWaitRunnable(MatchManager matchManager, Match match, int minPlayer){
        this.matchManager = matchManager;
        this.match = match;
        this.minPlayer = minPlayer;
    }


    @Override
    public void run() {

        if(matchManager.getJoinedPlayers().size() < minPlayer){
            timeLeft = DEFAULT_WAIT_TIME;
        }

        //スコアボード
        SclatScoreboard scoreboard = matchManager.getScoreboard();
        for(SclatPlayer sclatPlayer : matchManager.getJoinedPlayers()) {
            Lang lang = sclatPlayer.getLang();
            List<String> lines = new ArrayList<>();
            lines.add("");
            lines.add("§a" + MessageManager.getText(lang, "match-map") + " » §r§l" + match.getSclatMap().getDisplayName(lang));
            lines.add(" ");
            lines.add("§a" + MessageManager.getText(lang, "match-mode") + " » §6§l" + match.getType().getDisplayName(lang));
            lines.add("   ");
            if(matchManager.getJoinedPlayers().size() < minPlayer)
                lines.add("§b" + String.format(MessageManager.getText(lang, "match-wait-player"), minPlayer - matchManager.getJoinedPlayers().size()));
            else
                lines.add("§b" + MessageManager.getText(lang, "match-wait-time") + " » §r§l" + timeLeft + MessageManager.getText(lang, "word-sec"));
            scoreboard.setSidebarLine(sclatPlayer, lines);
        }
        scoreboard.updateSidebar(matchManager.getJoinedPlayers());

        if(timeLeft == 0){
            //開始処理
            
            if(matchManager.getType() == MatchManager.MatchManageType.NAWABARI) {
                SclatTeam team0 = match.getSclatTeams().get(0);
                SclatTeam team1 = match.getSclatTeams().get(1);
    
                int index = 0;
                for (SclatPlayer sclatPlayer : matchManager.getJoinedPlayers()) {
                    if(index % 2 == 0){
                        team0.join(sclatPlayer);
                    }else{
                        team1.join(sclatPlayer);
                    }
                    sclatPlayer.setScoreBoard(match.getScoreboard());
                    index++;
                }
            }
            
            IntroManager.playIntro(match);
            this.cancel();
        }
        timeLeft--;
    }
    
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 20);
    }
}
