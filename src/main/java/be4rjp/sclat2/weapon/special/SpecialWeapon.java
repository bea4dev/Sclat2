package be4rjp.sclat2.weapon.special;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;

public abstract class SpecialWeapon extends SclatWeapon {
    
    public SpecialWeapon(String id) {
        super(id);
    }
    
    @Override
    public abstract void onRightClick(SclatPlayer sclatPlayer);
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer) {
    
    }
}
