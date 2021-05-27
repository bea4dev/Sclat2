package be4rjp.sclat2.player;

import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.util.SclatParticle;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.main.runnable.MainWeaponRunnable;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
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
    //塗りポイント
    private int paints = 0;
    //キルカウント
    private int kills = 0;
    //メインウエポンのスケジューラーのマップ
    private final Map<MainWeapon, MainWeaponRunnable> mainWeaponTaskMap = new ConcurrentHashMap<>();
    
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
    
    public int getPaints() {return paints;}
    
    public int getKills() {return kills;}
    
    public synchronized void addPaints(int paints) {this.paints += paints;}
    
    public synchronized void addKills(int kills) {this.kills += kills;}
    
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
     * パケットを送信します
     * @param packet 送信するパケット
     */
    public void sendPacket(Packet packet){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }
    
    /**
     * プレイヤーの座標を返します
     * @return Location
     */
    public Location getLocation(){
        Player player = this.getBukkitPlayer();
        if(player == null){
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }else{
            return player.getLocation();
        }
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
    
    /**
     * メインウエポンのスケジューラーのマップを取得する
     * @return Map<MainWeapon, MainWeaponRunnable>
     */
    public Map<MainWeapon, MainWeaponRunnable> getMainWeaponTaskMap(){return this.mainWeaponTaskMap;}
    
    /**
     * 音を再生する
     * @param sound SclatSound
     */
    public void playSound(SclatSound sound){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        sound.play(player, player.getLocation());
    }
    
    /**
     * 音を再生する
     * @param sound SclatSound
     * @param location 音を再生する座標
     */
    public void playSound(SclatSound sound, Location location){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        sound.play(player, location);
    }
    
    /**
     * パーティクルを表示する
     * @param particle SclatParticle
     * @param location パーティクルを表示する座標
     */
    public void spawnParticle(SclatParticle particle, Location location){
        Player player = this.getBukkitPlayer();
        if(player == null) return;
        particle.spawn(player, location);
    }
}
