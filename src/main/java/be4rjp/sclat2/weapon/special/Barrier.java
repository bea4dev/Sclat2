package be4rjp.sclat2.weapon.special;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.special.runnable.BarrierRunnable;

public class Barrier extends SpecialWeapon{

    public Barrier(String id) {
        super(id);

        for(Lang lang : Lang.values()){
            this.displayName.put(lang, MessageManager.getText(lang, "sp-barrier"));
        }
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        sclatPlayer.setBarrier(true);
        new BarrierRunnable(sclatPlayer).start();
    }
}
