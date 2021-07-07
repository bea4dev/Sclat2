package be4rjp.sclat2.entity.sub;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.SclatEntity;
import be4rjp.sclat2.entity.SclatEntityTickRunnable;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.LocationUtil;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.sub.SubWeapon;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SplashBombEntity implements SclatEntity {
    
    private static final Set<BlockFace> REFLECT_FACE = new HashSet<>(Arrays.asList(BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST));
    private static final SclatSound WARNING_SOUND = new SclatSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1.6F);
    
    private final SclatTeam sclatTeam;
    private final EntityItem entityItem;
    private final SclatEntityTickRunnable runnable;
    private final SclatPlayer sclatPlayer;

    private Location location;
    private Vector vector;
    private Set<SclatPlayer> showPlayer = new HashSet<>();
    
    private int ground = 0;
    private int tick = 0;
    private boolean onGround = false;
    

    public SplashBombEntity(Location location, Vector vector, SclatTeam sclatTeam, SclatPlayer sclatPlayer){
        this.sclatTeam = sclatTeam;
        this.location = location;
        this.vector = vector;
        this.sclatPlayer = sclatPlayer;
        
        this.entityItem = new EntityItem(((CraftWorld)location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        entityItem.setItemStack(CraftItemStack.asNMSCopy(new ItemStack(sclatTeam.getSclatColor().getWool())));
        entityItem.setMot(new Vec3D(vector.getX(), vector.getY(), vector.getZ()));
        
        this.runnable = new SclatEntityTickRunnable(this);
    }


    @Override
    public void tick() {
        
        if(ground >= 30 || tick > 2000){
            remove();
        }
        
        if(entityItem.locY() < 0){
            runnable.cancel();
        }
        
        Location loc = new Location(location.getWorld(), entityItem.locX(), entityItem.locY(), entityItem.locZ());
        if(ground > 10 && ground < 20 && ground % 2 == 0){
            showPlayer.forEach(op -> op.playSound(WARNING_SOUND, loc));
        }
    
        showPlayer.forEach(this::sendVelocityPacket);
        if(tick % 15 == 0 && tick != 0){
            showPlayer.forEach(this::sendTeleportPacket);
        }
        
        try {
            entityItem.tick();
        }catch (Exception e){
            runnable.cancel();
        }
        
        if(entityItem.onGround){
            ground++;
        }
        
        tick++;
    }

    @Override
    public int getEntityID() {
        return entityItem.getId();
    }

    @Override
    public void spawn() {
        for(SclatPlayer sclatPlayer : sclatTeam.getMatch().getPlayers()) {
            Player player = sclatPlayer.getBukkitPlayer();
            if (player == null) continue;
        
            Location playerLoc = sclatPlayer.getLocation();
            if (LocationUtil.distanceSquaredSafeDifferentWorld(playerLoc, location) > ENTITY_DRAW_DISTANCE_SQUARE) continue;
        
            showPlayer.add(sclatPlayer);
            sendSpawnPacket(sclatPlayer);
        }
        runnable.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
    }

    @Override
    public void remove() {
        SubWeapon splash_bomb = (SubWeapon) SclatWeapon.getSclatWeapon("SPLASH_BOMB");
        Location center = new Location(location.getWorld(), entityItem.locX(), entityItem.locY(), entityItem.locZ());
        SclatWeapon.createInkExplosion(sclatPlayer, splash_bomb, center, 5, 15);
        
        showPlayer.forEach(this::sendDestroyPacket);
        runnable.cancel();
    }
    
    @Override
    public boolean isDead() {
        return runnable.isCancelled();
    }
    
    
    public void sendSpawnPacket(SclatPlayer sclatPlayer){
        PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(entityItem);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityItem.getId(), entityItem.getDataWatcher(), true);
        sclatPlayer.sendPacket(spawnEntity);
        sclatPlayer.sendPacket(metadata);
    }
    
    public void sendTeleportPacket(SclatPlayer sclatPlayer){
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entityItem);
        sclatPlayer.sendPacket(teleport);
    }
    
    public void sendVelocityPacket(SclatPlayer sclatPlayer){
        PacketPlayOutEntityVelocity velocity = new PacketPlayOutEntityVelocity(entityItem);
        sclatPlayer.sendPacket(velocity);
    }
    
    public void sendDestroyPacket(SclatPlayer sclatPlayer){
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityItem.getId());
        sclatPlayer.sendPacket(destroy);
    }
}
