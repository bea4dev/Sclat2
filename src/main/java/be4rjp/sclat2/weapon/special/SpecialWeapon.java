package be4rjp.sclat2.weapon.special;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.inventory.ItemStack;

public abstract class SpecialWeapon extends SclatWeapon {
    
    public SpecialWeapon(String id) {
        super(id);
    }
    
    @Override
    public abstract void onRightClick(SclatPlayer sclatPlayer);
    
    public abstract ItemStack getItemStack(Lang lang);
}
