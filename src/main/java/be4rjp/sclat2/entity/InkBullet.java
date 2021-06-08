package be4rjp.sclat2.entity;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.event.AsyncInkHitBlockEvent;
import be4rjp.sclat2.event.AsyncInkHitPlayerEvent;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.BlockParticle;
import be4rjp.sclat2.util.BoundingBox;
import be4rjp.sclat2.util.RayTrace;
import be4rjp.sclat2.util.SclatParticle;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.main.Shooter;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class InkBullet implements SclatEntity{
    
    private final Match match;
    private final SclatTeam team;
    private Location location;
    private final EntitySnowball snowball;
    private final MainWeapon mainWeapon;
    
    private SclatPlayer shooter;
    private Vector direction = new Vector(0, 0, 0);
    private boolean hitSound = false;
    private boolean hitParticle = false;
    private SclatParticle particle;
    
    private SclatEntityTickRunnable tickRunnable = null;
    private int tick = 0;
    private int fallTick = 0;
    
    public InkBullet(SclatTeam team, Location location, MainWeapon mainWeapon){
        this.team = team;
        this.match = team.getMatch();
        this.location = location;
        this.mainWeapon = mainWeapon;
    
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle();
        this.snowball = new EntitySnowball(EntityTypes.SNOWBALL, nmsWorld);
        
        this.particle = new BlockParticle(Particle.BLOCK_DUST, 0, 0, -1, 0, 1, team.getSclatColor().getWool().createBlockData());
    }
    
    
    public void shootInitialize(SclatPlayer shooter, Vector direction, int fallTick){
        this.direction = direction.clone();
        this.shooter = shooter;
        this.fallTick = fallTick;
        
        if(shooter.getSclatTeam() != null){
            this.setItemStack(new ItemStack(shooter.getSclatTeam().getSclatColor().getWool()));
        }
    }
    
    
    public void setItemStack(ItemStack itemStack){this.snowball.setItem(CraftItemStack.asNMSCopy(itemStack));}
    
    public SclatPlayer getShooter(){return this.shooter;}
    
    public boolean isHitParticle() {return hitParticle;}
    
    public boolean isHitSound() {return hitSound;}
    
    public void setHitParticle(boolean hitParticle) {this.hitParticle = hitParticle;}
    
    public void setHitSound(boolean hitSound) {this.hitSound = hitSound;}
    
    public MainWeapon getMainWeapon(){return mainWeapon;}
    
    
    @Override
    public void tick() {
        if(tick >= 2000){
            if(tickRunnable != null) tickRunnable.cancel();
            remove();
        }
    
        
        try {
            RayTraceResult rayTraceResult = location.getWorld().rayTraceBlocks(location, direction, direction.length());
            if (rayTraceResult != null) {
                Location hitLocation = rayTraceResult.getHitPosition().toLocation(location.getWorld());
                AsyncInkHitBlockEvent hitBlockEvent = new AsyncInkHitBlockEvent(this, rayTraceResult.getHitBlock(), hitLocation);
                Sclat.getPlugin().getServer().getPluginManager().callEvent(hitBlockEvent);
                this.remove();
                return;
            }
        }catch (Exception e){e.printStackTrace();}
    
        RayTrace rayTrace = new RayTrace(location.toVector(), direction);
        for(SclatPlayer sclatPlayer : match.getPlayers()) {
            Player player = sclatPlayer.getBukkitPlayer();
            if(player == null) continue;
            if(sclatPlayer.getSclatTeam() == null) continue;
            
            BoundingBox boundingBox = new BoundingBox(player, 0.2);
            if(!rayTrace.intersects(boundingBox, direction.length(), 0.1)) continue;
            if(this.team == sclatPlayer.getSclatTeam()) continue;
    
            AsyncInkHitPlayerEvent event = new AsyncInkHitPlayerEvent(this, sclatPlayer);
            Sclat.getPlugin().getServer().getPluginManager().callEvent(event);
            this.remove();
            return;
        }
        
        if(tick == fallTick){
            direction.multiply(direction.length() / 15);
        }
        
        if(tick >= fallTick && tick <= fallTick + 10) {
            //direction.normalize().multiply(0.9);
            //direction.add(new Vector(0, -0.21, 0));
            direction = direction.add(new Vector(0, -0.12, 0));
        }
        location.add(direction);
    
        snowball.setPosition(location.getX(), location.getY(), location.getZ());
        snowball.setMot(direction.getX(), direction.getY(), direction.getZ());
        
        boolean sendTeleport = tick % 30 == 0;
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(snowball);
        PacketPlayOutEntityVelocity velocity = new PacketPlayOutEntityVelocity(snowball);
        for(SclatPlayer sclatPlayer : match.getPlayers()){
            Player player = sclatPlayer.getBukkitPlayer();
            if(player == null) continue;
            
            Location playerLoc = sclatPlayer.getLocation();
            if(playerLoc.distanceSquared(location) > ENTITY_DRAW_DISTANCE_SQUARE) continue;
            if(sendTeleport) sclatPlayer.sendPacket(teleport);
            sclatPlayer.sendPacket(velocity);
        }
        
        if(tick % 3 == 0) match.spawnParticle(particle, location);
        
        tick++;
    }
    
    @Override
    public int getEntityID() {
        return snowball.getId();
    }
    
    @Override
    public void spawn() {
        PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(snowball);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(snowball.getId(), snowball.getDataWatcher(), true);
        for(SclatPlayer sclatPlayer : match.getPlayers()) {
            Player player = sclatPlayer.getBukkitPlayer();
            if (player == null) continue;
    
            Location playerLoc = sclatPlayer.getLocation();
            if (playerLoc.distanceSquared(location) > ENTITY_DRAW_DISTANCE_SQUARE) continue;
            sclatPlayer.sendPacket(spawnEntity);
            sclatPlayer.sendPacket(metadata);
        }
        tickRunnable = new SclatEntityTickRunnable(this);
        tickRunnable.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
    }
    
    @Override
    public void remove() {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(snowball.getId());
        for(SclatPlayer sclatPlayer : match.getPlayers()) {
            Player player = sclatPlayer.getBukkitPlayer();
            if (player == null) continue;
    
            Location playerLoc = sclatPlayer.getLocation();
            if (playerLoc.distanceSquared(location) > ENTITY_DRAW_DISTANCE_SQUARE) continue;
            sclatPlayer.sendPacket(destroy);
        }
        
        try{
            tickRunnable.cancel();
        }catch (Exception e){/**/}
    }
}
