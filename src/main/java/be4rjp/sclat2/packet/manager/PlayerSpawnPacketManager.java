package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.player.SclatPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;

import java.lang.reflect.Field;
import java.util.UUID;

public class PlayerSpawnPacketManager {
    private static Field b;
    
    static {
        try{
            b = PacketPlayOutNamedEntitySpawn.class.getDeclaredField("b");
            b.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }
    
    
    public static boolean write(PacketPlayOutNamedEntitySpawn spawnPacket, SclatPlayer sclatPlayer){
        try {
            if(sclatPlayer.getSclatTeam() == null) return true;
            
            UUID uuid = (UUID) b.get(spawnPacket);
            if(uuid == null) return true;
            if(!SclatPlayer.isCreated(uuid.toString())) return true;
            
            SclatPlayer op = SclatPlayer.getSclatPlayer(uuid.toString());
            if(op.getSclatTeam() == null) return true;
            
            switch (sclatPlayer.getObservableOption()){
                case ALL_PLAYER:{
                    return true;
                }
                
                case ONLY_MATCH_PLAYER:{
                    return sclatPlayer.getSclatTeam().getMatch() == op.getSclatTeam().getMatch();
                }
                
                case ALONE:{
                    return false;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return true;
    }
}
