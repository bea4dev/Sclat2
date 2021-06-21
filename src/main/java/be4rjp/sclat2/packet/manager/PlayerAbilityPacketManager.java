package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.player.SclatPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutAbilities;

import java.lang.reflect.Field;

public class PlayerAbilityPacketManager {
    private static Field f;
    
    static {
        try{
            f = PacketPlayOutAbilities.class.getDeclaredField("f");
            f.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }
    
    public static void write(PacketPlayOutAbilities packet, SclatPlayer sclatPlayer){
        try {
            f.set(packet, sclatPlayer.getFOV());
        }catch (Exception e){e.printStackTrace();}
    }
}
