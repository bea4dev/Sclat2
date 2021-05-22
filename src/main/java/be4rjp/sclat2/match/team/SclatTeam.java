package be4rjp.sclat2.match.team;

import be4rjp.sclat2.match.Match;

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
}
