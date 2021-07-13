package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.entity.ink.InkBullet;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.PlayerSquidRunnable;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.BoundingBox;
import be4rjp.sclat2.util.RayTrace;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.util.particle.BlockParticle;
import be4rjp.sclat2.util.particle.SclatParticle;
import be4rjp.sclat2.weapon.main.Roller;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class RollerRunnable extends MainWeaponRunnable{
    
    private static final SclatSound NO_INK_SOUND = new SclatSound(Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, 0.8F, 1.2F);
    
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
        
        if(taskTick == shootTick && !roll && !(sclatPlayer.isSquid() && sclatPlayer.isOnInk())){
            Vector direction = sclatPlayer.getEyeLocation().getDirection().normalize();
            Location location = sclatPlayer.getEyeLocation();
    
            Vector xzVector = new Vector(direction.getX(), 0, direction.getZ());
            float xzAngle = xzVector.angle(new Vector(0, 0, 1)) * (direction.getX() >= 0 ? 1 : -1);
            Vector X = new Vector(1, 0, 0);
            X.rotateAroundY(xzAngle);
            Vector Y = direction.clone();
            Y.rotateAroundAxis(X, Math.toRadians(270));
            
            boolean enoughInk = true;
            
            for(double angle = -roller.getShootMaxAngle(); angle <= roller.getShootMaxAngle(); angle += roller.getShootAccuracy()){
                if(sclatPlayer.consumeInk(roller.getNeedInk())) {
                    InkBullet inkBullet = new InkBullet(sclatTeam, location, roller);
                    double range = Math.random() / 4;
                    Vector randomVector = new Vector(Math.random() * range - range / 8.0, Math.random() * range - range / 8.0, Math.random() * range - range / 8.0);
                    inkBullet.shootInitialize(sclatPlayer, direction.clone().rotateAroundAxis(Y, Math.toRadians(angle)).add(randomVector).multiply(roller.getShootSpeed()), roller.getFallTick());
                    inkBullet.spawn();
                }else{
                    enoughInk = false;
                }
            }
    
            for (double ud : new double[]{-0.1, 0.1}) {
                Vector ud_direction = direction.clone().add(new Vector(0.0, ud, 0.0));
                for (double angle = -roller.getShootDoubleMaxAngle(); angle <= roller.getShootDoubleMaxAngle(); angle += roller.getShootAccuracy()) {
                    if(sclatPlayer.consumeInk(roller.getNeedInk())) {
                        InkBullet inkBullet = new InkBullet(sclatTeam, location, roller);
                        double range = Math.random() / 5;
                        Vector randomVector = new Vector(Math.random() * range - range / 10.0, Math.random() * range - range / 10.0, Math.random() * range - range / 10.0);
                        inkBullet.shootInitialize(sclatPlayer, ud_direction.clone().rotateAroundAxis(Y, Math.toRadians(angle)).add(randomVector).multiply(roller.getShootSpeed()), roller.getFallTick());
                        inkBullet.spawn();
                    }else{
                        enoughInk = false;
                    }
                }
            }
            
            if(!enoughInk){
                sclatPlayer.playSound(NO_INK_SOUND);
            }
            
            sclatTeam.getMatch().playSound(roller.getShootSound());
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
    
                    Vector vec1 = new Vector(XZVector.getZ() * -1, 0, XZVector.getX());
                    Vector vec2 = new Vector(XZVector.getZ(), 0, XZVector.getX() * -1);
                    RayTrace rayTrace1 = new RayTrace(location.clone().add(XZVector).toVector(), vec1);
                    RayTrace rayTrace2 = new RayTrace(location.clone().add(XZVector).toVector(), vec2);
    
                    //エフェクト
                    ArrayList<Vector> positions = rayTrace1.traverse(roller.getRollWidth(), 0.5);
                    positions.addAll(rayTrace2.traverse(roller.getRollWidth(), 0.5));
                    for (Vector position : positions) {
        
                        for (int y = position.getBlockY(); y > 0; y--) {
                            Location loc = position.toLocation(location.getWorld());
                            loc.setY(y);
                            if (loc.getBlock().getType().toString().endsWith("AIR")) continue;
            
                            sclatTeam.getMatch().paint(sclatPlayer, loc, 1.5);
                            break;
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
                        if (!rayTrace1.intersects(boundingBox, roller.getRollWidth(), 0.5)) continue;
                        if (!rayTrace2.intersects(boundingBox, roller.getRollWidth(), 0.5)) continue;
                        if (sclatTeam == op.getSclatTeam()) continue;
        
                        op.giveDamage(roller.getRollDamage(), sclatPlayer, direction, roller);
                    }
                    
                }
            }
        }
        
        if(taskTick >= shootTick){
            if(!using){
                if(roll && !(sclatPlayer.isSquid() && sclatPlayer.isOnInk())){
                    sclatPlayer.setWalkSpeed(defaultPlayerSpeed);
                }
                roll = false;
                shoot = false;
            }
        }
        
        
        taskTick++;
        playerTick++;
    }
}
