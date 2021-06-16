package be4rjp.sclat2.match;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.block.PaintData;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchRunnable;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.block.BlockUpdater;
import be4rjp.sclat2.util.SclatParticle;
import be4rjp.sclat2.util.SclatScoreboard;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.util.SphereBlocks;
import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scoreboard.*;

import java.util.*;
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
    protected List<SclatTeam> sclatTeams = new ArrayList<>();
    //この試合のブロックアップデーター
    protected BlockUpdater blockUpdater = new BlockUpdater(this);
    //塗られたブロックとその情報のマップ
    protected Map<Block, PaintData> paintDataMap = new ConcurrentHashMap<>();
    //SclatTeamとスコアボードのチームのマップ
    protected Map<SclatTeam, Team> teamMap = new HashMap<>();
    
    //試合のスコアボード
    protected final SclatScoreboard scoreboard;
    

    public Match(SclatMap sclatMap){
        this.sclatMap = sclatMap;

        this.scoreboard = new SclatScoreboard("§6§lSclat2§r " + Sclat.VERSION, 10);
    }

    public abstract MatchType getType();
    
    /**
     * 試合を開始
     */
    public void start(){
        this.matchRunnable.start();
        this.startBlockUpdate();
    }
    
    /**
     * 試合の初期化およびセットアップ
     */
    public abstract void initialize();
    
    /**
     * この試合のゲームマップを取得する
     * @return SclatMap
     */
    public SclatMap getSclatMap() {return sclatMap;}
    
    /**
     * この試合のスコアボードを取得する
     * @return Scoreboard
     */
    public SclatScoreboard getScoreboard() {return scoreboard;}
    
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
     * 引き分けの場合はnull
     * @return SclatTeam
     */
    public abstract SclatTeam getWinner();
    
    
    /**
     * この試合に存在する全てのチームを取得する
     * @return List<SclatTeam>
     */
    public List<SclatTeam> getSclatTeams() {return sclatTeams;}
    
    
    /**
     * チームを追加します
     * @param sclatTeam 追加するチーム
     */
    public void addSclatTeam(SclatTeam sclatTeam){this.sclatTeams.add(sclatTeam);}
    
    
    /**
     * プレイヤーをチームのスポーン場所にテレポートさせます
     * @param sclatPlayer
     */
    public void teleportToTeamLocation(SclatPlayer sclatPlayer){
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam == null) return;
    
        int index = this.getSclatTeams().indexOf(sclatTeam);
        sclatPlayer.teleport(sclatMap.getTeamLocation(index));
    }


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
    public synchronized void paint(SclatPlayer sclatPlayer, Location center, double radius){
        SphereBlocks sphereBlocks = new SphereBlocks(radius, center);
        Set<Block> blocks = sphereBlocks.getBlocks();
        int paint = blocks.size();
        sclatPlayer.addPaints(paint);
        SclatTeam team = sclatPlayer.getSclatTeam();
        team.addPaints(paint);
        for (Block block : blocks) {
            if(paintDataMap.containsKey(block)){
                PaintData paintData = paintDataMap.get(block);
                if(paintData.getSclatTeam() != team) {
                    paintData.getSclatTeam().addPaints(-1);
                    paintData.setSclatTeam(team);
                    team.addPaints(1);
                    blockUpdater.setBlock(block, team.getSclatColor().getWool());
                }
            }else {
                PaintData paintData = new PaintData(this, block, team);
                paintData.getSclatTeam().addPaints(1);
                blockUpdater.setBlock(block, team.getSclatColor().getWool());
            }
        }
    }
    
    
    public enum MatchType{
        LOBBY("§6§lロビー", "§6§lLobby"),
        NAWABARI("§6§lナワバリバトル", "§6§lTurf War");
        
        private final HashMap<Lang, String> displayName;
    
        MatchType(String ja_JP, String en_US){
            displayName = new HashMap<>();
            displayName.put(Lang.ja_JP, ja_JP);
            displayName.put(Lang.en_US, en_US);
        }
        
        /**
         * 表示名を取得する
         * @return String
         */
        public String getDisplayName(Lang lang) {
            String name = displayName.get(lang);
            if(name == null){
                return "No name.";
            }else{
                return name;
            }
        }
    }
}
