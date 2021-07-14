package be4rjp.sclat2.player.costume;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.SclatEntity;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.LocationUtil;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class HeadUpCostume extends BukkitRunnable {

    private static Field a;
    private static Field b;

    static {
        try {
            a = PacketPlayOutMount.class.getDeclaredField("a");
            b = PacketPlayOutMount.class.getDeclaredField("b");
            a.setAccessible(true);
            b.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }

    private final SclatPlayer sclatPlayer;
    private final SclatTeam sclatTeam;
    private final Match match;
    private final ItemStack itemStack;
    private final Set<SclatPlayer> showPlayer;
    private final EntityArmorStand armorStand;

    public HeadUpCostume(SclatPlayer sclatPlayer, SclatTeam sclatTeam, ItemStack itemStack){
        this.sclatPlayer = sclatPlayer;
        this.sclatTeam = sclatTeam;
        this.match = sclatTeam.getMatch();
        this.itemStack = itemStack;
        this.showPlayer = new HashSet<>();

        Location loc = sclatPlayer.getLocation();
        this.armorStand = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setNoGravity(true);
    }


    @Override
    public void run() {
        for(SclatPlayer matchPlayer : match.getPlayers()){
            if(LocationUtil.distanceSquaredSafeDifferentWorld(sclatPlayer.getLocation(), matchPlayer.getLocation()) < SclatEntity.ENTITY_DRAW_DISTANCE_SQUARE){
                if(!showPlayer.contains(matchPlayer)){
                    this.sendSpawnPacket(matchPlayer);
                    showPlayer.add(matchPlayer);
                }
            }else{
                if(showPlayer.contains(matchPlayer)){
                    this.sendDestroyPacket(matchPlayer);
                    showPlayer.remove(matchPlayer);
                }
            }
        }
    }


    public void sendSpawnPacket(SclatPlayer target){
        PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack));
        PacketPlayOutMount mount = new PacketPlayOutMount();
        try {
            a.set(mount, sclatPlayer.getEntityID());
            b.set(mount, new int[]{armorStand.getId()});
        }catch (Exception e){e.printStackTrace();}

        target.sendPacket(spawn);
        target.sendPacket(metadata);
        target.sendPacket(equipment);
        target.sendPacket(mount);
    }

    public void sendDestroyPacket(SclatPlayer target){
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(armorStand.getId());
        target.sendPacket(destroy);
    }


    public void spawn(){
        try {
            this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 60);
        }catch (Exception e){/**/}
    }


    public void remove(){
        try{
            this.cancel();
        }catch (Exception e){/**/}
        this.showPlayer.forEach(this::sendDestroyPacket);
    }
}
