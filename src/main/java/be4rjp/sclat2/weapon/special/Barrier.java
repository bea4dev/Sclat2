package be4rjp.sclat2.weapon.special;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.special.runnable.BarrierRunnable;

public class Barrier extends SpecialWeapon{
    public Barrier(String id) {
        super(id);
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        sclatPlayer.setBarrier(true);
        new BarrierRunnable(sclatPlayer).runTaskLaterAsynchronously(Sclat.getPlugin(), 80);
    }
}
