package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.entity.ink.InkBullet;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.main.FixedRateShooter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Timer;
import java.util.TimerTask;

public class FixedRateShooterRunnable extends MainWeaponRunnable {
    
    private static final SclatSound NO_INK_SOUND = new SclatSound(Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, 0.8F, 1.2F);
    
    private final FixedRateShooter shooter;
    private final Timer timer;
    private boolean setup = false;
    
    
    public FixedRateShooterRunnable(FixedRateShooter shooter, SclatPlayer sclatPlayer){
        super(shooter, sclatPlayer);
        this.shooter = shooter;
        this.timer = new Timer(true);
        
        soundTick = shooter.getShootTick() % 2 == 0 ? shooter.getShootTick() / 2 : shooter.getShootTick() / 2 + 1;
    }
    
    private int taskTick = 0;
    private int clickTick = 0;
    private int noClickTick = 0;
    
    private int soundTaskTick = 0;
    private boolean using = false;
    private boolean noInk = false;
    private final int soundTick;
    
    @Override
    public void run() {
        if(!setup){
            setup = true;
            
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    using = playerTick <= 12;
                    
                    if(taskTick % shooter.getShootTick() == 0){
                        if(using) {
                            Player player = sclatPlayer.getBukkitPlayer();
                            if (player == null) return;
                            //インク消費
                            if(sclatPlayer.consumeInk(shooter.getNeedInk())) {
                                noInk = false;
                                
                                //射撃
                                Vector direction = player.getEyeLocation().getDirection();
                                Location origin = player.getEyeLocation();
                                
                                InkBullet inkBullet = new InkBullet(sclatPlayer.getSclatTeam(), origin, shooter);
                                double range = shooter.getRecoil().getShootRandomRange(clickTick);
                                Vector randomVector = new Vector(Math.random() * range - range / 2, 0, Math.random() * range - range / 2);
                                inkBullet.shootInitialize(sclatPlayer, direction.multiply(shooter.getShootSpeed()).add(randomVector), shooter.getFallTick());
                                inkBullet.spawn();
                                
                                clickTick += shooter.getShootTick();
                            }else{
                                noInk = true;
                            }
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
            };
            timer.scheduleAtFixedRate(timerTask, 0, 25);
        }
        
        if(soundTaskTick % soundTick == 0) {
            if (using) {
                if(noInk){
                    sclatPlayer.playSound(NO_INK_SOUND);
                }else{
                    sclatPlayer.playSound(shooter.getShootSound());
                }
            }
        }
        soundTaskTick++;
    }
    
    
    @Override
    public synchronized void cancel() throws IllegalStateException {
        try {
            timer.cancel();
        }catch (Exception e){e.printStackTrace();}
        super.cancel();
    }
}
