package be4rjp.sclat2.weapon.special.runnable;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.costume.HeadUpCostume;
import be4rjp.sclat2.util.SclatSound;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BarrierRunnable extends BukkitRunnable {

    private static final SclatSound END_SPECIAL_WEAPON = new SclatSound(Sound.BLOCK_CHEST_CLOSE, 1.0F, 2.0F);
    
    private final SclatPlayer sclatPlayer;
    private final HeadUpCostume headUpCostume;
    private int tick = 0;
    
    public BarrierRunnable(SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;

        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        ItemStack glass = new ItemStack(sclatTeam.getSclatColor().getGlass());
        this.headUpCostume = new HeadUpCostume(sclatPlayer, sclatTeam, glass);
    }
    
    @Override
    public void run() {
        if(tick == 0){
            this.headUpCostume.spawn();
            sclatPlayer.setBarrier(true);
            sclatPlayer.getSPWeaponProgress().setPaintLock(true);
        }

        sclatPlayer.getSPWeaponProgress().setProgress(100 - ((tick / 40) * 100));

        if(tick == 40) {//4秒後にバリア解除
            sclatPlayer.setBarrier(false);
            headUpCostume.remove();
            sclatPlayer.getSPWeaponProgress().initialize();
            sclatPlayer.playSound(END_SPECIAL_WEAPON);
            this.cancel();
        }

        tick++;
    }

    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
}
