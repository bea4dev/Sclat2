package be4rjp.sclat2.weapon;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.LocationUtil;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.util.math.Sphere;
import be4rjp.sclat2.util.particle.BlockParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SclatWeapon {
    //識別IDと武器のマップ
    private static Map<String, SclatWeapon> weaponMap = new ConcurrentHashMap<>();
    
    public static void initialize(){
        weaponMap.clear();
    }
    
    /**
     * 識別IDから武器を取得する
     * @param id 識別ID
     * @return SclatWeapon
     */
    public static SclatWeapon getSclatWeapon(String id){
        return weaponMap.get(id);
    }
    
    /**
     * 武器を登録する
     * @param id 識別ID
     * @param sclatWeapon 登録する武器のインスタンス
     */
    public static void registerSclatWeapon(String id, SclatWeapon sclatWeapon){
        weaponMap.put(id, sclatWeapon);
    }
    
    
    public static Collection<SclatWeapon> getWeaponList(){return weaponMap.values();}
    
    
    
    //武器の識別名
    protected final String id;
    //武器の表示名
    protected Map<Lang, String> displayName = new HashMap<>();
    //一発分の必要インク量
    protected float needInk = 0.0F;
    //一発分のダメージ
    protected float damage = 0.0F;
    
    public SclatWeapon(String id){
        this.id = id;
        weaponMap.put(id, this);
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

    
    public String getId(){return id;}
    
    /**
     * 一発分のダメージを取得する
     * @return double
     */
    public float getDamage() {return damage;}

    /**
     * 一発分の必要インク量を取得する
     * @return float
     */
    public float getNeedInk() {return needInk;}
    
    /**
     * この武器を持って右クリックしたときの処理
     * @param sclatPlayer
     */
    public abstract void onRightClick(SclatPlayer sclatPlayer);
    
    /**
     * この武器を持って左クリックしたときの処理
     * @param sclatPlayer
     */
    public abstract void onLeftClick(SclatPlayer sclatPlayer);
    
    
    
    private static SclatSound EXPLOSION_SOUND = new SclatSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 0.8F);
    
    /**
     * インクの爆発を作成する
     * @param sclatPlayer 爆発を起こしたプレイヤー
     * @param sclatWeapon 爆発を起こした武器
     * @param center 爆発の中心
     * @param radius 爆発の半径
     * @param effectAccuracy エフェクトの細かさ(度数指定)
     */
    public static void createInkExplosion(SclatPlayer sclatPlayer, SclatWeapon sclatWeapon, Location center, double radius, int effectAccuracy){
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam == null) return;
    
        //エフェクト
        org.bukkit.block.data.BlockData bd = sclatTeam.getSclatColor().getWool().createBlockData();
        Set<Location> sphere = Sphere.getSphere(center, radius - 1, effectAccuracy);
        for(Location loc : sphere){
            sclatTeam.getMatch().spawnParticle(new BlockParticle(Particle.BLOCK_DUST, 0, loc.getX() - center.getX(), loc.getY() - center.getY(), loc.getZ() - center.getZ(), 1, bd), loc);
        }
        
        //塗り
        sclatTeam.getMatch().paint(sclatPlayer, center, radius - 1);
        
        //音
        sclatTeam.getMatch().playSound(EXPLOSION_SOUND, center);
        
        //ダメージ
        for(SclatPlayer otherTeamPlayer : sclatTeam.getOtherTeamPlayers()){
            if(otherTeamPlayer.isDeath()) continue;
            
            double distance = Math.sqrt(LocationUtil.distanceSquaredSafeDifferentWorld(center, otherTeamPlayer.getLocation()));
            if(distance >= radius) continue;
    
            Location loc = otherTeamPlayer.getLocation();
            Vector velocity = new Vector(loc.getX() - center.getX(), loc.getY() - center.getY(), loc.getZ() - center.getZ());
            double damage = ((radius - distance) / radius) * sclatWeapon.getDamage();
            otherTeamPlayer.giveDamage((float) damage, sclatPlayer, velocity, sclatWeapon);
        }
    }
}
