package be4rjp.sclat2.player;

import be4rjp.sclat2.match.team.SclatTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * プレイヤーへの処理の全般は基本的にこのクラスで行う
 */
public class SclatPlayer {
    
    private static Map<String, SclatPlayer> playerMap = new ConcurrentHashMap<>();
    
    /**
     * 指定されたUUIDのSclatPlayerを返します。
     * 存在しなければ新しく作成し登録した後、返します。
     * @param uuid プレイヤーのUUID
     * @return SclatPlayer
     */
    public static SclatPlayer getSclatPlayer(String uuid){
        if(playerMap.containsKey(uuid)){
            return playerMap.get(uuid);
        }else{
            SclatPlayer sclatPlayer = new SclatPlayer(uuid);
            playerMap.put(uuid, sclatPlayer);
            return sclatPlayer;
        }
    }
    
    /**
     * 指定されたプレイヤーからSclatPlayerを返します。
     * 存在しなければ新しく作成し登録した後、返します。
     * @param player プレイヤー
     * @return SclatPlayer
     */
    public static SclatPlayer getSclatPlayer(Player player) {
        return getSclatPlayer(player.getUniqueId().toString());
    }
    
    
    
    //プレイヤーのUUID
    private final String uuid;
    //プレイヤー
    private Player player = null;
    //所属しているチーム
    private SclatTeam sclatTeam = null;
    
    /**
     * SclatPlayerを新しく作成
     * @param uuid プレイヤーのUUID
     */
    public SclatPlayer(String uuid){
        this.uuid = uuid;
    }
    
    
    public String getUUID() {return uuid;}
    
    public SclatTeam getSclatTeam() {return sclatTeam;}
    
    public void setSclatTeam(SclatTeam sclatTeam) {this.sclatTeam = sclatTeam;}
    
    /**
     * BukkitのPlayerを取得します。
     * @return Player
     */
    public Player getBukkitPlayer(){
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player == null) return this.player;
        
        if(this.player == null){
            this.player = player;
        }else{
            if(this.player != player){
                this.player = player;
            }
        }
        return player;
    }
    
    
    /**
     * メッセージを送信します
     * @param message メッセージ
     */
    public void sendMessage(String message){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        player.sendMessage("[§6Sclat§r] " + message);
    }
    
    
    /**
     * 非同期でテレポートさせます。
     * @param location テレポート先
     */
    public void teleportAsync(Location location){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        player.teleportAsync(location);
    }
}
