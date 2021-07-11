package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.entity.ink.InkBullet;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.PlayerSquidRunnable;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.BoundingBox;
import be4rjp.sclat2.util.RayTrace;
import be4rjp.sclat2.util.particle.BlockParticle;
import be4rjp.sclat2.util.particle.SclatParticle;
import be4rjp.sclat2.weapon.main.Roller;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class RollerRunnable extends MainWeaponRunnable{
    
    private final Roller roller;
    private final SclatParticle ROLL_PARTICLE;
    private final SclatTeam sclatTeam;
    
    public RollerRunnable(Roller roller, SclatPlayer sclatPlayer, SclatTeam sclatTeam) {
        super(roller, sclatPlayer);
        this.roller = roller;
        this.sclatTeam = sclatTeam;
        
        this.ROLL_PARTICLE = new BlockParticle(Particle.BLOCK_DUST, 1, 0, 0, 0, 1, sclatTeam.getSclatColor().getWool().createBlockData());
    }
    
    
    private int taskTick = 0;
    private int shootTick = 0;
    private boolean shoot = false;
    private boolean roll = false;
    private float defaultPlayerSpeed = 0.1F;
    
    @Override
    public void run() {
        boolean using = playerTick <= 6 && !sclatPlayer.isSquid();
    
        if(using){
            if(!shoot){
                shoot = true;
                shootTick = taskTick + roller.getShootTick();
            }
        }
        
        if(taskTick == shootTick && !roll){
            shoot = false;
            Vector direction = sclatPlayer.getEyeLocation().getDirection();
            Location location = sclatPlayer.getEyeLocation();
            for(double angle = -roller.getShootMaxAngle(); angle <= roller.getShootMaxAngle(); angle += roller.getShootAccuracy()){
                InkBullet inkBullet = new InkBullet(sclatTeam, location, roller);
                inkBullet.shootInitialize(sclatPlayer, direction.clone().rotateAroundY(Math.toRadians(angle)).multiply(roller.getShootSpeed()), roller.getFallTick());
                inkBullet.spawn();
            }
    
            for(double angle = -roller.getShootDoubleMaxAngle(); angle <= roller.getShootDoubleMaxAngle(); angle += roller.getShootAccuracy()){
                InkBullet inkBullet = new InkBullet(sclatTeam, location, roller);
                inkBullet.shootInitialize(sclatPlayer, direction.clone().rotateAroundY(Math.toRadians(angle)).multiply(roller.getShootSpeed()), roller.getFallTick());
                inkBullet.spawn();
            }
        }
        
        if(taskTick >= shootTick && using){
            if(!roll){
                defaultPlayerSpeed = sclatPlayer.getWalkSpeed();
                sclatPlayer.setWalkSpeed(roller.getRollWalkSpeed());
            }
            roll = true;
            
            if(sclatPlayer.consumeInk(roller.getRollNeedInk())) {
                Vector direction = sclatPlayer.getEyeLocation().getDirection();
                Location location = sclatPlayer.getLocation().clone().add(0.0, 0.5, 0.0);
    
                Vector XZVector = new Vector(direction.getX(), 0.0, direction.getZ());
                if (XZVector.lengthSquared() > 0.0) {
                    XZVector = XZVector.normalize();
    
                    for (double angle : new double[]{-90.0, 90.0}) {
                        RayTrace rayTrace = new RayTrace(location.clone().add(XZVector).toVector(), XZVector.clone().rotateAroundY(Math.toRadians(angle)));
        
                        //エフェクト
                        ArrayList<Vector> positions = rayTrace.traverse(roller.getRollWidth(), 0.5);
                        for (Vector position : positions) {
            
                            for (int y = position.getBlockY(); y > 0; y--) {
                                Location loc = position.toLocation(location.getWorld());
                                loc.setY(y);
                                if (loc.getBlock().getType().toString().endsWith("AIR")) continue;
                
                                sclatTeam.getMatch().paint(sclatPlayer, loc, 1.5);
                            }
            
                            //エフェクト
                            if (taskTick % 2 == 0) {
                                sclatTeam.getMatch().getPlayers().forEach(matchPlayer -> matchPlayer.spawnParticle(ROLL_PARTICLE, position.toLocation(location.getWorld())));
                            }
                        }
        
        
                        for (SclatPlayer op : sclatTeam.getMatch().getPlayers()) {
                            Player player = op.getBukkitPlayer();
                            if (player == null) continue;
                            if (op.getSclatTeam() == null) continue;
                            if (op.isDeath()) continue;
            
                            BoundingBox boundingBox = new BoundingBox(player, 0.2);
                            if (!rayTrace.intersects(boundingBox, roller.getRollWidth(), 0.5)) continue;
                            if (sclatTeam == op.getSclatTeam()) continue;
            
                            op.giveDamage(roller.getRollDamage(), sclatPlayer, direction, roller);
                        }
                    }
                }
            }
        }else{
            if(roll){
                sclatPlayer.setWalkSpeed(defaultPlayerSpeed);
            }
            roll = false;
        }
        
        
        taskTick++;
        playerTick++;
    }
}
