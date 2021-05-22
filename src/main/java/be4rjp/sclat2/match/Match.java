package be4rjp.sclat2.match;

import be4rjp.sclat2.match.team.SclatTeam;

import java.util.Set;

/**
 * 各試合の親クラス
 */
public abstract class Match {
    
    //試合のスケジューラー
    protected MatchRunnable matchRunnable;
    //この試合のチーム
    protected Set<SclatTeam> sclatTeams;
    
    
    /**
     * 試合終了判定
     * @return boolean 試合が終了したかどうか
     */
    public abstract boolean checkWin();
    
    
    /**
     * 試合に勝利したチームを取得する。
     * @return SclatTeam
     */
    public abstract SclatTeam getWinner();
    
    
    /**
     * この試合に存在する全てのチームを取得する
     * @return
     */
    public Set<SclatTeam> getSclatTeams() {return sclatTeams;}
}
