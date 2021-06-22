package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.player.SclatPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutUpdateHealth;

import java.lang.reflect.Field;

public class HealthUpdatePacketManager {
    
    private static Field a;
    private static Field b;
    
    static {
        try{
            a = PacketPlayOutUpdateHealth.class.getDeclaredField("a");
            b = PacketPlayOutUpdateHealth.class.getDeclaredField("b");
            a.setAccessible(true);
            b.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }
    
    public static void write(PacketPlayOutUpdateHealth packet, SclatPlayer sclatPlayer){
        try {
            a.set(packet, sclatPlayer.getHealth());
            b.set(packet, sclatPlayer.getFoodLevel());
        }catch (Exception e){e.printStackTrace();}
    }
    
}
