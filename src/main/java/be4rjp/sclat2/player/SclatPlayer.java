package be4rjp.sclat2.player;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.message.MessageManager;
import be4rjp.sclat2.util.SclatParticle;
import be4rjp.sclat2.util.SclatScoreboard;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.main.runnable.MainWeaponRunnable;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_15_R1.PacketPlayOutUpdateHealth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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
    public synchronized static SclatPlayer getSclatPlayer(String uuid){
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
    public synchronized static SclatPlayer getSclatPlayer(Player player) {
        return getSclatPlayer(player.getUniqueId().toString());
    }
    
    //アーマーがインクをはじく音
    private static final SclatSound REPEL_SOUND = new SclatSound(Sound.ENTITY_SPLASH_POTION_BREAK, 1F, 1.5F);
    //プレイヤーが攻撃をヒットさせた時に鳴らす通知音
    private static final SclatSound HIT_SOUND_FOR_ATTACKER = new SclatSound(Sound.ENTITY_PLAYER_HURT, 0.5F, 1F);
    //プレイヤーが攻撃を受けた時に鳴らす音
    private static final SclatSound HIT_SOUND = new SclatSound(Sound.ENTITY_PLAYER_HURT, 1F, 1F);
    
    
    //プレイヤーのUUID
    private final String uuid;
    //プレイヤーの言語設定
    private Lang lang = Lang.ja_JP;
    //プレイヤー
    private Player player = null;
    //所属しているチーム
    private SclatTeam sclatTeam = null;
    //スコアボード
    private SclatScoreboard scoreBoard;
    //塗りポイント
    private int paints = 0;
    //キルカウント
    private int kills = 0;
    //メインウエポンのスケジューラーのマップ
    private final Map<MainWeapon, MainWeaponRunnable> mainWeaponTaskMap = new ConcurrentHashMap<>();
    //プレイヤーの体力
    private float health = 20.0F;
    //プレイヤーのアーマー値
    private float armor = 0.0F;
    
    /**
     * SclatPlayerを新しく作成
     * @param uuid プレイヤーのUUID
     */
    private SclatPlayer(String uuid){
        this.uuid = uuid;
    }
    
    
    public String getUUID() {return uuid;}
    
    public Lang getLang() {return lang;}
    
    public void setLang(Lang lang) {this.lang = lang;}
    
    public SclatTeam getSclatTeam() {return sclatTeam;}
    
    public void setSclatTeam(SclatTeam sclatTeam) {this.sclatTeam = sclatTeam;}
    
    public int getPaints() {return paints;}
    
    public int getKills() {return kills;}
    
    public synchronized float getArmor() {return armor;}
    
    public synchronized void setArmor(float armor) {this.armor = armor;}
    
    public synchronized float getHealth() {return health;}
    
    public synchronized void setHealth(float health) {this.health = health;}
    
    public synchronized void addPaints(int paints) {this.paints += paints;}
    
    public synchronized void addKills(int kills) {this.kills += kills;}
    
    public SclatScoreboard getScoreBoard() {return scoreBoard;}
    
    public void setScoreBoard(SclatScoreboard scoreBoard) {
        this.scoreBoard = scoreBoard;
        if(player != null) player.setScoreboard(scoreBoard.getBukkitScoreboard());
    }
    
    /**
     * BukkitのPlayerを取得します。
     * @return Player
     */
    public Player getBukkitPlayer(){
        return player;
    }
    
    
    /**
     * BukkitのPlayerをアップデートする（再参加用）
     */
    public void updateBukkitPlayer(){
        this.player = Bukkit.getPlayer(UUID.fromString(uuid));
    }
    
    
    /**
     * メッセージを送信します。
     * 基本的にメッセージを送信するときはsendText()を使用してください。
     * @param message メッセージ
     */
    @Deprecated
    public void sendMessage(String message){
        if(player == null) return;
        player.sendMessage("[§6Sclat§r] " + message);
    }
    
    /**
     * 言語別のメッセージを送信します。
     * @param lang 言語
     * @param textName message.ymlに設定されているテキストの名前
     */
    public void sendText(Lang lang, String textName){
        if(player == null) return;
        player.sendMessage("[§6Sclat§r] " + MessageManager.getText(lang, textName));
    }
    
    /**
     * 言語別のメッセージを送信します。
     * @param textName message.ymlに設定されているテキストの名前
     */
    public void sendText(String textName){
        if(player == null) return;
        player.sendMessage("[§6Sclat§r] " + MessageManager.getText(lang, textName));
    }
    
    /**
     * パケットを送信します
     * @param packet 送信するパケット
     */
    public void sendPacket(Packet packet){
        if(player == null) return;
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }
    
    /**
     * プレイヤーの座標を返します
     * @return Location
     */
    public Location getLocation(){
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
        if(player == null) return;
        player.teleportAsync(location);
    }
    
    /**
     * メインウエポンのスケジューラーのマップを取得する
     * @return Map<MainWeapon, MainWeaponRunnable>
     */
    public Map<MainWeapon, MainWeaponRunnable> getMainWeaponTaskMap(){return this.mainWeaponTaskMap;}
    
    /**
     * メインウエポンのスケジューラーを全て停止して削除する
     */
    public void clearMainWeaponTasks(){
        for(MainWeaponRunnable runnable : mainWeaponTaskMap.values()){
            try{
                runnable.cancel();
            }catch (Exception e){/**/}
        }
        
        mainWeaponTaskMap.clear();
    }
    
    /**
     * 音を再生する
     * @param sound SclatSound
     */
    public void playSound(SclatSound sound){
        if(player == null) return;
        sound.play(player, player.getLocation());
    }
    
    /**
     * 音を再生する
     * @param sound SclatSound
     * @param location 音を再生する座標
     */
    public void playSound(SclatSound sound, Location location){
        if(player == null) return;
        sound.play(player, location);
    }
    
    /**
     * パーティクルを表示する
     * @param particle SclatParticle
     * @param location パーティクルを表示する座標
     */
    public void spawnParticle(SclatParticle particle, Location location){
        if(player == null) return;
        particle.spawn(player, location);
    }
    
    
    /**
     * プレイヤーにダメージを与える
     * @param damage 与えるダメージ
     * @param attacker 攻撃者
     */
    public synchronized void giveDamage(float damage, SclatPlayer attacker){
        if(player == null) return;
        if(attacker.getBukkitPlayer() == null) return;
        if(this.sclatTeam == null) return;
        
        if(this.getHealth() + this.getArmor() > damage){
            if(this.getArmor() > damage){
                this.setArmor(this.getArmor() - damage);
                this.sclatTeam.getMatch().playSound(REPEL_SOUND, player.getLocation());
            }else{
                //give damage
                float d = damage - this.getArmor();
                this.setHealth(this.getHealth() - d);
                this.setArmor(0.0F);
    
                PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(getHealth(), 20, 5.0F);
                PacketPlayOutAnimation animation = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 1);
                this.sendPacket(updateHealth);
                this.sclatTeam.getMatch().sendPacket(animation);
    
                attacker.playSound(HIT_SOUND_FOR_ATTACKER);
                this.sclatTeam.getMatch().playSound(HIT_SOUND, player.getLocation());
            }
        }else{
            this.sendMessage("死んでしまうとは情けない！");
            this.sendMessage("Killed by: " + attacker.getDisplayName());
            this.setHealth(20.0F);
            //死亡処理
        }
    }
    
    
    /**
     * プレイヤーの表示名を取得する
     * @return String
     */
    public String getDisplayName(){
        if(player == null) return "";
        if(sclatTeam == null) player.getName();
        
        return sclatTeam.getSclatColor().getChatColor() + player.getName() + "§r";
    }
}
