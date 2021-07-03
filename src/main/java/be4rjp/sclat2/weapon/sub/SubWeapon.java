package be4rjp.sclat2.weapon.sub;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.inventory.ItemStack;

public abstract class SubWeapon extends SclatWeapon {
    
    public SubWeapon(String id) {
        super(id);
    }
    
    @Override
    public abstract void onRightClick(SclatPlayer sclatPlayer);
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer) {
    
    }
    
    public abstract ItemStack getItemStack(SclatTeam sclatTeam, Lang lang);
}
