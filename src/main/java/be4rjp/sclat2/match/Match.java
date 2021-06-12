package be4rjp.sclat2.match;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.block.PaintData;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchRunnable;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.block.BlockUpdater;
import be4rjp.sclat2.util.SclatParticle;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.util.SphereBlocks;
import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 各試合の親クラス
 */
public abstract class Match {
    
    //この試合のマップ
    protected final SclatMap sclatMap;
    //試合のスケジューラー
    protected MatchRunnable matchRunnable;
    //この試合のチーム
    protected Set<SclatTeam> sclatTeams = new HashSet<>();
    //この試合のブロックアップデーター
    protected BlockUpdater blockUpdater = new BlockUpdater(this);
    //塗られたブロックとその情報のマップ
    protected Map<Block, PaintData> paintDataMap = new ConcurrentHashMap<>();
    //SclatTeamとスコアボードのチームのマップ
    protected Map<SclatTeam, Team> teamMap = new HashMap<>();
    
    //試合のスコアボード
    protected final Scoreboard scoreboard;
    

    public Match(SclatMap sclatMap){
        this.sclatMap = sclatMap;
    
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getNewScoreboard();
    }
    
    
    public abstract MatchType getType();
    
    /**
     * この試合のスコアボードを取得する
     * @return Scoreboard
     */
    public Scoreboard getScoreboard() {return scoreboard;}
    
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
    
    
    /**
     * この試合に参加しているプレイヤー全員に音を聞かせる
     * @param sound サウンド
     * @param location 再生する座標
     */
    public void playSound(SclatSound sound, Location location){this.getPlayers().forEach(sclatPlayer -> sclatPlayer.playSound(sound, location));}
    
    /**
     * この試合に参加しているプレイヤー全員に音を聞かせる
     * @param sound サウンド
     */
    public void playSound(SclatSound sound){this.getPlayers().forEach(sclatPlayer -> sclatPlayer.playSound(sound));}
    
    /**
     * この試合に参加しているプレイヤー全員にパーティクルを表示する
     * @param particle パーティクル
     * @param location パーティクルを表示する座標
     */
    public void spawnParticle(SclatParticle particle, Location location){this.getPlayers().forEach(sclatPlayer -> sclatPlayer.spawnParticle(particle, location));}
    
    /**
     * この試合に参加しているプレイヤー全員にパケットを送信する
     * @param packet 送信するパケット
     */
    public void sendPacket(Packet packet){this.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendPacket(packet));}
    
    /**
     * 球状に塗る
     * @param sclatPlayer 塗るプレイヤー
     * @param center 塗る中心座標
     * @param radius 塗る半径
     */
    public void paint(SclatPlayer sclatPlayer, Location center, double radius){
        SphereBlocks sphereBlocks = new SphereBlocks(radius, center);
        Set<Block> blocks = sphereBlocks.getBlocks();
        int paint = blocks.size();
        sclatPlayer.addPaints(paint);
        SclatTeam team = sclatPlayer.getSclatTeam();
        team.addPaints(paint);
        blocks.forEach(block -> blockUpdater.setBlock(block, team.getSclatColor().getWool()));
    }
    
    
    public enum MatchType{
        LOBBY,
        PVP_2_TEAM
    }
}
