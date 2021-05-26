package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.main.Shooter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShooterRunnable extends MainWeaponRunnable {
    
    private final Shooter shooter;
    
    public ShooterRunnable(Shooter shooter, SclatPlayer sclatPlayer){
        super(shooter, sclatPlayer);
        this.shooter = shooter;
    }
    
    private int taskTick = 0;
    private int playerTick = 10;
    private int clickTick = 0;
    private int noClickTick = 0;
    
    public void setPlayerTick(int tick){
        this.playerTick = tick;
    }
    
    @Override
    public void run() {
        boolean using = playerTick <= 6;
        
        if(taskTick % shooter.getShootTick() == 0){
            if(using) {
                Player player = sclatPlayer.getBukkitPlayer();
                if (player == null) return;
                //射撃
                InkBullet inkBullet = new InkBullet(sclatPlayer.getSclatTeam().getMatch(), player.getEyeLocation(), shooter);
                double range = shooter.getRecoil().getShootRandomRange(clickTick);
                Vector randomVector = new Vector(Math.random() * range - range/2, Math.random() * (range/2) - range/4, Math.random() * range - range/2);
                inkBullet.shootInitialize(sclatPlayer, player.getEyeLocation().getDirection().multiply(shooter.getShootSpeed()).add(randomVector), shooter.getFallTick());
                inkBullet.spawn();
                sclatPlayer.playSound(shooter.getShootSound());
                
                clickTick += shooter.getShootTick();
            }else{
                noClickTick += shooter.getShootTick();
                if(noClickTick >= shooter.getRecoil().getResetTick()) {
                    noClickTick = 0;
                    clickTick = 0;
                }
            }
        }
        
        
        playerTick++;
        taskTick++;
    }
}
