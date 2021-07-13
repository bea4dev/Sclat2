package be4rjp.sclat2.weapon.main.runnable;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.PlayerSquidRunnable;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.LocationUtil;
import be4rjp.sclat2.util.particle.BlockParticle;
import be4rjp.sclat2.util.BoundingBox;
import be4rjp.sclat2.util.RayTrace;
import be4rjp.sclat2.util.particle.RedStoneDust;
import be4rjp.sclat2.util.particle.SclatParticle;
import be4rjp.sclat2.weapon.main.Charger;
import be4rjp.sclat2.weapon.main.ui.ChargerUI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ChargerRunnable extends MainWeaponRunnable{
    
    private final Charger charger;
    private final SclatTeam sclatTeam;
    private final SclatParticle CHARGE_PARTICLE;
    private final SclatParticle SHOOT_PARTICLE;
    
    private int charge = 0;
    private int tick = 0;
    
    public ChargerRunnable(Charger charger, SclatPlayer sclatPlayer, SclatTeam sclatTeam) {
        super(charger, sclatPlayer);
        this.charger = charger;
        this.sclatTeam = sclatTeam;
    
        Particle.DustOptions dustOptions = new Particle.DustOptions(sclatTeam.getSclatColor().getBukkitColor(), 0.5F);
        CHARGE_PARTICLE = new RedStoneDust(Particle.REDSTONE, 1, 0, 0, 0, 50, dustOptions);
        SHOOT_PARTICLE = new BlockParticle(Particle.BLOCK_DUST, 1, 0, 0, 0, 1, sclatTeam.getSclatColor().getWool().createBlockData());
    }
    
    @Override
    public void run() {
        if(!sclatPlayer.getMainWeaponTaskMap().containsValue(this)) this.shoot();
    
        ChargerUI.ChargerUIInfo info = charger.getChargerUI().getInfo(charge);
        if(charger.isScope() && info != null){
            sclatPlayer.setFOV(info.fov);
            sclatPlayer.sendIconTitle(info.title, info.subtitle, 0, 20, 0);
        }
    
        if(tick % 2 == 0 && !sclatPlayer.isSquid()) {
            Location start = sclatPlayer.getEyeLocation();
            Vector direction = start.getDirection();
            Location blockHit = null;
            RayTraceResult rayTraceResult = start.getWorld().rayTraceBlocks(start, direction, charger.getReach() * (double) charge);
            if (rayTraceResult != null) {
                blockHit = rayTraceResult.getHitPosition().toLocation(start.getWorld());
            }
            
            RayTrace rayTrace = new RayTrace(sclatPlayer.getEyeLocation().toVector(), direction);
            ArrayList<Vector> positions = rayTrace.traverse(blockHit == null ? charger.getReach() * (double) charge : Math.sqrt(LocationUtil.distanceSquaredSafeDifferentWorld(blockHit, start)), 1.5);
    
            for (Vector vector : positions) {
                Location position = vector.toLocation(sclatPlayer.getLocation().getWorld());
                for(SclatPlayer matchPlayer : sclatTeam.getMatch().getPlayers()){
                    if(sclatPlayer != matchPlayer) matchPlayer.spawnParticle(CHARGE_PARTICLE, position);
                }
            }
        }
        
        if(charge < charger.getMaxCharge() && !sclatPlayer.isSquid()) charge++;
        
        tick++;
    }
    
    
    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        sclatPlayer.resetTitle();
        sclatPlayer.setFOV(PlayerSquidRunnable.NORMAL_DEFAULT_FOV);
    }
    
    public void shoot(){
        try {
            this.cancel();
            if(charger.isScope()) sclatPlayer.resetTitle();
            sclatPlayer.clearMainWeaponTasks();
        }catch (Exception e){/**/}
        
        Match match = sclatTeam.getMatch();
        match.playSound(charger.getShootSound(), sclatPlayer.getLocation());
        
        if(sclatTeam == Sclat.getLobbyTeam()){
            sclatPlayer.setFOV(0.1F);
        }else {
            sclatPlayer.setFOV(PlayerSquidRunnable.NORMAL_DEFAULT_FOV);
        }
    
        Location start = sclatPlayer.getEyeLocation();
        Vector direction = start.getDirection();
        Location blockHit = null;
        RayTraceResult rayTraceResult = start.getWorld().rayTraceBlocks(start, direction, charger.getReach() * (double) charge);
        if (rayTraceResult != null) {
            blockHit = rayTraceResult.getHitPosition().toLocation(start.getWorld());
        }
        
        
        RayTrace rayTrace = new RayTrace(sclatPlayer.getEyeLocation().toVector(), direction);
        double reach = charger.getReach() * (double)charge;
        ArrayList<Vector> positions = rayTrace.traverse(reach, 0.5);
    
        for (Vector vector : positions) {
            Location position = vector.toLocation(sclatPlayer.getLocation().getWorld());
            match.spawnParticle(SHOOT_PARTICLE, position);
    
            boolean hit = false;
            if(blockHit != null) {
                if (LocationUtil.distanceSquaredSafeDifferentWorld(blockHit, start) < LocationUtil.distanceSquaredSafeDifferentWorld(start, position)){
                    hit = true;
                }
            }
            
            for (int y = position.getBlockY(); y > 0; y--) {
                Location loc = position.clone();
                loc.setY(y);
                if (loc.getBlock().getType().toString().endsWith("AIR")) continue;
            
                sclatTeam.getMatch().paint(sclatPlayer, loc, charger.getPaintRadius());
                if(!hit) {
                    break;
                }else{
                    if(y == position.getBlockY() - 5) break;
                }
            }
            
            if(hit){
                break;
            }
        }
    
        for (SclatPlayer op : match.getPlayers()) {
            Player player = op.getBukkitPlayer();
            if (player == null) continue;
            if (op.getSclatTeam() == null) continue;
            if (op.isDeath()) continue;
    
            BoundingBox boundingBox = new BoundingBox(player, charger.getBulletSize() + (sclatPlayer.isBarrier() ? 0.5 : 0.0));
            if (!rayTrace.intersects(boundingBox, charger.getReach() * (double)charge, 0.5)) continue;
            if (sclatTeam == op.getSclatTeam()) continue;
            
            op.giveDamage((float)(charger.getDamage() * (double)charge), sclatPlayer, direction, charger);
        }
    }
}
