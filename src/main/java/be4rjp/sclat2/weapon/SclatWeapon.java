package be4rjp.sclat2.weapon;

import be4rjp.sclat2.player.SclatPlayer;

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
