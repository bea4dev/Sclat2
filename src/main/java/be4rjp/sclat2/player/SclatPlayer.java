package be4rjp.sclat2.player;

import be4rjp.cinema4c.util.SkinManager;
import be4rjp.parallel.ParallelWorld;
import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.data.WeaponPossessionData;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.MatchManager;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.death.DeathType;
import be4rjp.sclat2.player.death.PlayerDeathManager;
import be4rjp.sclat2.player.passive.Gear;
import be4rjp.sclat2.player.passive.PassiveInfluence;
import be4rjp.sclat2.util.particle.SclatParticle;
import be4rjp.sclat2.util.SclatScoreboard;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.WeaponClass;
import be4rjp.sclat2.weapon.main.runnable.MainWeaponRunnable;
import io.papermc.lib.PaperLib;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * プレイヤーへの処理の全般は基本的にこのクラスで行う
 */
public class SclatPlayer {
    
    private static final Map<String, SclatPlayer> playerMap = new ConcurrentHashMap<>();
    
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
    
    /**
     * 指定されたUUIDのSclatPlayerが既に作成されているかどうかを取得します
     * @param uuid プレイヤーのUUID
     * @return 既に作成されている場合は true されていない場合は false
     */
    public synchronized static boolean isCreated(String uuid){
        return playerMap.containsKey(uuid);
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
    //所持している武器クラスのデータ
    private WeaponPossessionData weaponPossessionData = new WeaponPossessionData();
    //Parallel
    private ParallelWorld parallelWorld;
    //参加しているMatchManager
    private MatchManager matchManager = null;
    //所属しているチーム
    private SclatTeam sclatTeam = Sclat.getLobbyTeam();
    //スコアボード
    private SclatScoreboard scoreBoard = null;
    //クラス
    private WeaponClass weaponClass = null;
    //最後にテレポートを実行した時間
    private long teleportTime = 0;
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
    //インク残量
    private float ink = 1.0F;
    //クライアントの視野角
    private float field_of_view = 0.1F;
    //プレイヤーのスキンデータ
    private String[] skin = null;
    //どのプレイヤーを表示するかのオプション
    private ObservableOption observableOption = ObservableOption.ALL_PLAYER;
    //イカ状態であるかどうか
    private boolean isSquid = false;
    //インク上にいるかどうか
    private boolean isOnInk = false;
    //フライ状態であるかどうか
    private boolean isFly = false;
    //移動速度
    private float walkSpeed = 0.2F;
    //フードレベル
    private int foodLevel = 20;
    //死んでいるかどうか
    private boolean isDeath = false;
    //ギアのリスト
    private final List<Gear> gearList = new CopyOnWriteArrayList<>();
    //パッシブ効果
    private final PassiveInfluence passiveInfluence = new PassiveInfluence();

    //キルカウントの動作の同期用インスタンス
    private final Object KILL_COUNT_LOCK = new Object();
    //ペイントカウントの動作の同期用インスタンス
    private final Object PAINT_COUNT_LOCK = new Object();
    //インク系の動作の同期用インスタンス
    private final Object INK_LOCK = new Object();
    //フライ系の動作の同期用インスタンス
    private final Object FLY_LOCK = new Object();
    //死亡系の動作の同期用インスタンス
    private final Object DEATH_LOCK = new Object();
    
    
    /**
     * SclatPlayerを新しく作成
     * @param uuid プレイヤーのUUID
     */
    private SclatPlayer(String uuid){this.uuid = uuid;}
    
    
    public String getUUID() {return uuid;}
    
    public Lang getLang() {return lang;}
    
    public void setLang(Lang lang) {this.lang = lang;}
    
    public SclatTeam getSclatTeam() {return sclatTeam;}
    
    public void setSclatTeam(SclatTeam sclatTeam) {this.sclatTeam = sclatTeam;}
    
    public MatchManager getMatchManager() {return matchManager;}
    
    public void setMatchManager(MatchManager matchManager) {this.matchManager = matchManager;}
    
    public int getPaints() {synchronized (PAINT_COUNT_LOCK){return paints;}}
    
    public int getKills() {synchronized (KILL_COUNT_LOCK){return kills;}}
    
    public synchronized float getArmor() {return armor;}
    
    public synchronized void setArmor(float armor) {this.armor = armor;}
    
    public synchronized float getHealth() {return health;}
    
    public synchronized void setHealth(float health) {
        this.health = health;
        PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
        this.sendPacket(updateHealth);
    }
    
    public ParallelWorld getParallelWorld() {
        if(parallelWorld == null) parallelWorld = ParallelWorld.getParallelWorld(uuid);
        return parallelWorld;
    }
    
    public void addPaints(int paints) {synchronized (PAINT_COUNT_LOCK){this.paints += paints;}}
    
    public void addKills(int kills) {synchronized (KILL_COUNT_LOCK){this.kills += kills;}}
    
    public boolean isSquid() {return isSquid;}
    
    public void setSquid(boolean squid) {isSquid = squid;}
    
    public boolean isOnInk() {return isOnInk;}
    
    public void setOnInk(boolean onInk) {isOnInk = onInk;}

    public boolean isDeath() {return isDeath;}

    public void setDeath(boolean death) {isDeath = death;}

    public SclatScoreboard getScoreBoard() {return scoreBoard;}
    
    public String[] getSkin() {return skin;}
    
    public ObservableOption getObservableOption() {return observableOption;}
    
    public List<Gear> getGearList() {return gearList;}
    
    public WeaponClass getWeaponClass() {return weaponClass;}
    
    public PassiveInfluence getPassiveInfluence() {return passiveInfluence;}
    
    public WeaponPossessionData getWeaponPossessionData() {return weaponPossessionData;}
    
    public void setScoreBoard(SclatScoreboard scoreBoard) {
        this.scoreBoard = scoreBoard;
        if(player != null) player.setScoreboard(scoreBoard.getBukkitScoreboard());
    }
    
    /**
     * 情報をリセットする
     */
    public void reset(){
        this.matchManager = null;
        this.sclatTeam = Sclat.getLobbyTeam();
        this.scoreBoard = null;
        this.paints = 0;
        this.kills = 0;
        this.health = 20.0F;
        this.armor = 0.0F;
        this.ink = 1.0F;
        this.observableOption = ObservableOption.ALL_PLAYER;
        this.isSquid = false;
        this.isOnInk = false;
        this.isDeath = false;
        this.gearList.clear();
        this.setFOV(0.1F);
        this.setFly(false);
        this.setWalkSpeed(0.2F);
        this.setFoodLevel(20);
    }
    
    /**
     * BukkitのPlayerを取得します。
     * @return Player
     */
    public Player getBukkitPlayer(){
        return player;
    }
    
    /**
     * プレイヤーのエンティティIDを取得します
     * @return
     */
    public int getEntityID(){
        if(player == null) return 0;
        return player.getEntityId();
    }
    
    /**
     * プレイヤーがオンラインであるかどうかを取得する
     * @return
     */
    public boolean isOnline(){
        if(player == null) return false;
        return player.isOnline();
    }
    
    /**
     * BukkitのPlayerをアップデートする（参加時用）
     */
    public void updateBukkitPlayer(){
        Player bukkitPlayer = Bukkit.getPlayer(UUID.fromString(uuid));
        if(bukkitPlayer != null) this.player = bukkitPlayer;
    }
    
    /**
     * Mojangのセッションサーバーへスキンデータのリクエストを送信して取得する
     */
    public void sendSkinRequest(){
        new BukkitRunnable() {
            @Override
            public void run() {
                skin = SkinManager.getSkin(uuid);
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
    
    /**
     * 武器クラスをロードする
     */
    public void loadWeaponPossessionData(){
        this.weaponPossessionData.load_from_sql();
    }
    
    /**
     * クラスをセットする
     * @param weaponClass
     */
    public void setWeaponClass(WeaponClass weaponClass){
        this.weaponClass = weaponClass;
    }
    
    /**
     * クラスを装備させる
     */
    public void equipWeaponClass(){
        this.weaponClass.setWeaponClass(this);
    }
    
    /**
     * 表示するプレイヤーを設定する
     * @param option ObservableOption
     */
    public void setObservableOption(ObservableOption option){
        this.observableOption = option;
        
        switch (observableOption){
            case ALONE:{
                if(this.player == null) break;
                if(this.sclatTeam == null) break;
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player op : Bukkit.getServer().getOnlinePlayers()) {
                            if(player != op) player.hidePlayer(Sclat.getPlugin(), op);
                        }
                    }
                }.runTask(Sclat.getPlugin());
                break;
            }
            
            case ONLY_MATCH_PLAYER:{
                if(this.player == null) break;
                if(this.sclatTeam == null) break;
                Set<Player> hidePlayers = new HashSet<>();
                Set<Player> showPlayers = new HashSet<>();
                
                for(Player op : Bukkit.getServer().getOnlinePlayers()){
                    SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(op);
                    if(sclatPlayer.getSclatTeam() == null) continue;
                    
                    if(sclatPlayer.getSclatTeam().getMatch() == this.sclatTeam.getMatch()){
                        showPlayers.add(op);
                    }else{
                        hidePlayers.add(op);
                    }
                }
    
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player op : hidePlayers) {
                            if(player != op) player.hidePlayer(Sclat.getPlugin(), op);
                        }
                        for(Player op : showPlayers) {
                            if(player != op) player.showPlayer(Sclat.getPlugin(), op);
                        }
                    }
                }.runTask(Sclat.getPlugin());
                break;
            }
            
            case ALL_PLAYER:{
                if(this.player == null) break;
    
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player op : Bukkit.getServer().getOnlinePlayers()) {
                            if(player != op) player.showPlayer(Sclat.getPlugin(), op);
                        }
                    }
                }.runTask(Sclat.getPlugin());
                break;
            }
        }
    }
    
    /**
     * 装備しているギアやメインウエポンからパッシブ効果を作成します
     */
    public void createPassiveInfluence(){
        this.passiveInfluence.createPassiveInfluence(this);
    }
    
    /**
     * プレイヤーのインク値を取得します(0 ~ 1)
     * @return
     */
    public float getInk() {synchronized (INK_LOCK){ return ink; }}
    
    /**
     * インク値を設定して経験値バーのパケットを送信する
     * @param ink float(0 ~ 1)
     */
    public void setInk(float ink) {
        synchronized (INK_LOCK){
            this.ink = ink;
            PacketPlayOutExperience experience = new PacketPlayOutExperience(this.ink, (int)(this.ink * 100.0F), 0);
            this.sendPacket(experience);
        }
    }
    
    /**
     * インクを消費させます
     * @param CInk 消費させるインク float(0 ~ 1)
     * @return インクが足りていたかどうか
     */
    public boolean consumeInk(float CInk){
        synchronized (INK_LOCK) {
            if (this.getInk() < CInk) {
                this.sendTextTitle(null, "weapon-no-ink", 0, 40, 10);
                return false;
            } else {
                this.setInk(this.getInk() - CInk);
                return true;
            }
        }
    }
    
    /**
     * インクを追加します
     * @param addInk 追加するインク量 float(0 ~ 1)
     */
    public void addInk(float addInk){
        synchronized (INK_LOCK){
            if(this.getInk() + addInk >= 1.0F){
                this.setInk(1.0F);
            }else{
                this.setInk(this.getInk() + addInk);
            }
        }
    }
    
    /**
     * クライアントの視野角を取得します
     * @return float
     */
    public float getFOV() {return field_of_view;}
    
    /**
     * クライアントの視野角を変更します
     * @param field_of_view
     */
    public void setFOV(float field_of_view) {
        this.field_of_view = field_of_view;
        
        PlayerAbilities abilities = new PlayerAbilities();
        abilities.walkSpeed = field_of_view;
        PacketPlayOutAbilities abilitiesPacket = new PacketPlayOutAbilities(abilities);
        this.sendPacket(abilitiesPacket);
    }
    
    /**
     * プレイヤーの移動速度を取得する
     * @return float
     */
    public float getWalkSpeed() {return walkSpeed;}
    
    /**
     * プレイヤーの移動速度を設定する
     * @param speed
     */
    public void setWalkSpeed(float speed){
        if(player == null) return;
        this.walkSpeed = speed;
        player.setWalkSpeed(speed);
    }
    
    /**
     * プレイヤーの飛行状態を設定します
     * @param fly
     */
    public void setFly(boolean fly){
        synchronized (FLY_LOCK) {
            if (player == null) return;
            isFly = fly;
            player.setAllowFlight(fly);
            player.setFlying(fly);
        }
    }
    
    /**
     * プレイヤーが飛行状態であるかどうかを取得します
     * @return
     */
    public boolean isFly() {
        synchronized (FLY_LOCK) {
            return isFly;
        }
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
     * 言語別のメッセージを送信します。
     * @param textName message.ymlに設定されているテキストの名前
     * @param args 置き換える値 (%d等)
     */
    public void sendText(String textName, Object... args){
        if(player == null) return;
        player.sendMessage("[§6Sclat§r] " + String.format(MessageManager.getText(lang, textName), args));
    }
    
    /**
     * 言語別のタイトルメッセージを送信します
     * @param titleTextName message.ymlに設定されているタイトルテキストの名前
     * @param subTitleTextName message.ymlに設定されているサブタイトルテキストの名前
     * @param fadeIn 文字のフェードイン[tick]
     * @param stay 文字の表示時間[tick]
     * @param fadeOut 文字のフェードアウト[tick]
     */
    public void sendTextTitle(String titleTextName, String subTitleTextName, int fadeIn, int stay, int fadeOut){
        if(player == null) return;
        player.sendTitle(MessageManager.getText(lang, titleTextName), MessageManager.getText(lang, subTitleTextName), fadeIn, stay, fadeOut);
    }

    /**
     * 言語別のタイトルメッセージを送信します
     * @param titleTextName message.ymlに設定されているタイトルテキストの名前
     * @param titleArgs 置き換える値 (%d等)
     * @param subTitleTextName message.ymlに設定されているサブタイトルテキストの名前
     * @param subTitleArgs 置き換える値 (%d等)
     * @param fadeIn 文字のフェードイン[tick]
     * @param stay 文字の表示時間[tick]
     * @param fadeOut 文字のフェードアウト[tick]
     */
    public void sendTextTitle(String titleTextName, Object[] titleArgs, String subTitleTextName, Object[] subTitleArgs, int fadeIn, int stay, int fadeOut){
        if(player == null) return;
        player.sendTitle(String.format(MessageManager.getText(lang, titleTextName), titleArgs), String.format(MessageManager.getText(lang, subTitleTextName), subTitleArgs), fadeIn, stay, fadeOut);
    }
    
    /**
     * タイトルテキストをリセットします
     */
    public void resetTitle(){
        sendTextTitle("none", "none", 0, 0, 0);
    }
    
    /**
     * アイコンのタイトルを送信します
     * @param titleIcon タイトルに表示するアイコン
     * @param subtitleIcon サブタイトルに表示するアイコン
     * @param fadeIn 文字のフェードイン[tick]
     * @param stay 文字の表示時間[tick]
     * @param fadeOut 文字のフェードアウト[tick]
     */
    public void sendIconTitle(String titleIcon, String subtitleIcon, int fadeIn, int stay, int fadeOut){
        if(player == null) return;
        player.sendTitle(titleIcon, subtitleIcon, fadeIn, stay, fadeOut);
    }
    
    /**
     * タイトルメッセージを送信します
     * @param titleText タイトルテキスト
     * @param subTitleText サブタイトルテキスト
     * @param fadeIn 文字のフェードイン[tick]
     * @param stay 文字の表示時間[tick]
     * @param fadeOut 文字のフェードアウト[tick]
     */
    @Deprecated
    public void sendTitle(String titleText, String subTitleText, int fadeIn, int stay, int fadeOut){
        if(player == null) return;
        player.sendTitle(titleText, subTitleText, fadeIn, stay, fadeOut);
    }
    
    /**
     * 言語別のアクションバーメッセージを送信します。
     * @param textName message.ymlに設定されているテキストの名前
     */
    public void sendTextActionbar(String textName){
        if(player == null) return;
        player.sendActionBar(MessageManager.getText(lang, textName));
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
     * プレイヤーの目線の座標を返します
     * @return Location
     */
    public Location getEyeLocation(){
        if(player == null){
            return new Location(Bukkit.getWorld("world"), 0, 0, 0);
        }else{
            return player.getEyeLocation();
        }
    }
    
    /**
     * テレポートさせます。
     * @param location テレポート先
     */
    public void teleport(Location location){
        long time = System.currentTimeMillis();
        this.teleportTime = time;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null) return;
                if (time != teleportTime) return;
                PaperLib.teleportAsync(player, location);
            }
        }.runTask(Sclat.getPlugin());
    }
    
    
    /**
     * ゲームモードを変更します
     * @param gameMode 設定するゲームモード
     */
    public void setGameMode(GameMode gameMode){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null) return;
                player.setGameMode(gameMode);
            }
        }.runTask(Sclat.getPlugin());
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
     * プレイヤーを回復させる
     * @param plus
     */
    public synchronized void heal(float plus){
        if(player == null) return;
        if(this.sclatTeam == null) return;
        
        if(this.health + plus < 20.0F){
            this.health += plus;
            PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
            this.sendPacket(updateHealth);
        }else if(this.health != 20.0F){
            this.health = 20.0F;
            PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
            this.sendPacket(updateHealth);
        }
    }
    
    /**
     * プレイヤーに毒ダメージを与えます
     * @param damage
     */
    public synchronized void givePoisonDamage(float damage){
        if(player == null) return;
        if(this.sclatTeam == null) return;
        
        if(this.health > damage){
            this.health -= damage;
            PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
            PacketPlayOutAnimation animation = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 1);
            this.sendPacket(updateHealth);
            this.sclatTeam.getMatch().sendPacket(animation);
            this.sclatTeam.getMatch().playSound(HIT_SOUND, player.getLocation());
        }
    }
    
    /**
     * プレイヤーにダメージを与える
     * @param damage 与えるダメージ
     * @param attacker 攻撃者
     * @param sclatWeapon 攻撃に使用した武器
     */
    public synchronized void giveDamage(float damage, SclatPlayer attacker, Vector velocity, SclatWeapon sclatWeapon){
        if(player == null) return;
        if(attacker.getBukkitPlayer() == null) return;
        if(this.sclatTeam == null) return;
        
        if(this.getArmor() > 0.0 && velocity != null && player != null){
            Vector XZVec = new Vector(velocity.getX(), 0.0, velocity.getZ());
            if(XZVec.lengthSquared() > 0.0) XZVec.normalize();
            player.setVelocity(XZVec);
        }
        
        if(this.getHealth() + this.getArmor() > damage){
            if(this.getArmor() > damage){
                this.setArmor(this.getArmor() - damage);
                this.sclatTeam.getMatch().playSound(REPEL_SOUND, player.getLocation());
            }else{
                //give damage
                float d = damage - this.getArmor();
                this.setHealth(this.getHealth() - d);
                this.setArmor(0.0F);
    
                PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
                PacketPlayOutAnimation animation = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 1);
                this.sendPacket(updateHealth);
                this.sclatTeam.getMatch().sendPacket(animation);
    
                attacker.playSound(HIT_SOUND_FOR_ATTACKER);
                this.sclatTeam.getMatch().playSound(HIT_SOUND, player.getLocation());
            }
        }else{
            //死亡処理
            this.setHealth(20.0F);
            PlayerDeathManager.death(this, attacker, sclatWeapon, DeathType.KILLED_BY_PLAYER);
        }
    }
    
    /**
     * フードレベルを取得します
     * @return
     */
    public synchronized int getFoodLevel() {return foodLevel;}
    
    /**
     * フードレベルを設定します
     * @param foodLevel
     */
    public synchronized void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
        if(player == null) return;
        PacketPlayOutUpdateHealth updateHealth = new PacketPlayOutUpdateHealth(this.health, this.foodLevel, 0.0F);
        this.sendPacket(updateHealth);
    }
    
    /**
     * プレイヤーの表示名を取得する
     * @return String
     */
    public String getDisplayName(){
        return getDisplayName(false);
    }
    
    /**
     * プレイヤーの表示名を取得する
     * @return String
     */
    public String getDisplayName(boolean bold){
        if(player == null) return "";
        if(sclatTeam == null) player.getName();
        
        return sclatTeam.getSclatColor().getChatColor() + (bold ? "§l" : "") + player.getName() + "§r";
    }
}
