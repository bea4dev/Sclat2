package be4rjp.sclat2.block;

import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import org.bukkit.block.Block;

/**
 * どのチームがどのブロックに塗ったかの情報を格納するためのクラス
 */
public class PaintData {
    
    //このデータを使用している試合
    private final Match match;
    //このデータを適用するブロック
    private final Block block;
    //塗ったチーム
    private SclatTeam sclatTeam = null;
    
    
    /**
     * ペイントデータを作成する
     * @param match このデータを使用する試合
     * @param block このデータを適用するブロック
     * @param sclatTeam 塗ったチーム
     */
    public PaintData(Match match, Block block, SclatTeam sclatTeam){
        this.match = match;
        this.block = block;
        this.sclatTeam = sclatTeam;
    }
    
    public Match getMatch() {return match;}
    
    public Block getBlock() {return block;}
    
    public SclatTeam getSclatTeam() {return sclatTeam;}
    
    public void setSclatTeam(SclatTeam sclatTeam) {this.sclatTeam = sclatTeam;}
}
