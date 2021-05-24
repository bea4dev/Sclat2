package be4rjp.sclat2.match;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.block.PaintData;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.block.BlockUpdater;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 各試合の親クラス
 */
public abstract class Match {
    
    //試合のスケジューラー
    protected MatchRunnable matchRunnable;
    //この試合のチーム
    protected Set<SclatTeam> sclatTeams = new HashSet<>();
    //この試合のブロックアップデーター
    protected BlockUpdater blockUpdater = new BlockUpdater(this);
    //塗られたブロックとその情報のマップ
    protected Map<Block, PaintData> paintDataMap = new ConcurrentHashMap<>();

    public Match(){
    
    }


    /**
     * この試合のスケジューラーをスタートさせる
     */
    public void startMatchRunnable(){
        this.matchRunnable.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 20);
    }

    /**
     * この試合のスケジューラーを停止させる
     */
    public void stopMatchRunnable(){
        this.matchRunnable.cancel();
    }
    
    /**
     * ブロックのアップデートをスタートさせる
     */
    public void startBlockUpdate(){
        this.blockUpdater.start();
    }


    /**
     * ブロックアップデーターを取得する
     * @return BlockUpdater
     */
    public BlockUpdater getBlockUpdater() {return blockUpdater;}

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
     * @return Set<SclatTeam>
     */
    public Set<SclatTeam> getSclatTeams() {return sclatTeams;}
    
    
    /**
     * チームを追加します
     * @param sclatTeam 追加するチーム
     */
    public void addSclatTeam(SclatTeam sclatTeam){this.sclatTeams.add(sclatTeam);}


    /**
     * この試合に参加しているプレイヤーを取得する
     * @return Set<SclatPlayer>
     */
    public Set<SclatPlayer> getPlayers(){
        Set<SclatPlayer> players = new HashSet<>();
        sclatTeams.forEach(sclatTeam -> players.addAll(sclatTeam.getTeamMembers()));
        return players;
    }
}
