package be4rjp.sclat2.weapon;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.player.SclatPlayer;

import java.util.HashMap;
import java.util.Map;
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
}
