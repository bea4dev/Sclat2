package be4rjp.sclat2.weapon.sub;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;

public abstract class SubWeapon extends SclatWeapon {
    
    //使うのに必要なインク量
    protected float NEED_INK = 60.0F;
    
    public SubWeapon(String id) {
        super(id);
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
    
    }
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer) {
    
    }
    
    /**
     * 使うのに必要なインク量を取得する
     * @return float(必要なインク量)
     */
    public float getNeedInk() {
        return NEED_INK;
    }
}
