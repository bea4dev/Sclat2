package be4rjp.sclat2.match.team;

import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.player.SclatPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * チーム
 */
public class SclatTeam {
    
    //試合のインスタンス
    private final Match match;
    //チームカラー
    private final SclatColor sclatColor;
    //チームの塗りポイント
    private int paints = 0;
    //チームのキルカウント
    private int kills = 0;
    //チームメンバー
    private Set<SclatPlayer> teamMembers = new CopyOnWriteArraySet<>();
    
    /**
     * チームのインスタンス作成
     * @param match
     * @param sclatColor
     */
    public SclatTeam(Match match, SclatColor sclatColor){
        this.match = match;
        this.sclatColor = sclatColor;
    }
    
    
    public int getKills() {return kills;}
    
    public int getPaints() {return paints;}
    
    public Match getMatch() {return match;}
    
    public SclatColor getSclatColor() {return sclatColor;}

    /**
     * 同じ試合のほかのチームを取得する
     * @return Set<SclatTeam>
     */
    public Set<SclatTeam> getOtherTeam(){
        Set<SclatTeam> sclatTeams = new HashSet<>();
        for(SclatTeam sclatTeam : match.getSclatTeams()){
            if(sclatTeam != this) sclatTeams.add(sclatTeam);
        }
        return sclatTeams;
    }

    /**
     * チームメンバーを取得します
     * @return Set<SclatPlayer>
     */
    public Set<SclatPlayer> getTeamMembers(){
        return teamMembers;
    }
}
