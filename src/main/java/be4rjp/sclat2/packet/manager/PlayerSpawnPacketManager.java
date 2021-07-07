package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.ObservableOption;
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

            if(sclatPlayer.getObservableOption() == ObservableOption.ALL_PLAYER) return true;

            UUID uuid = (UUID) b.get(spawnPacket);
            if(uuid == null) return true;
            if(!SclatPlayer.isCreated(uuid.toString())) return true;

            SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
            if(sclatTeam == null){
                return sclatPlayer.getObservableOption() != ObservableOption.ALONE;
            }

            SclatPlayer op = SclatPlayer.getSclatPlayer(uuid.toString());
            SclatTeam otherTeam = op.getSclatTeam();
            if(otherTeam == null) return false;
            
            switch (sclatPlayer.getObservableOption()){
                case ONLY_MATCH_PLAYER:{
                    return sclatTeam.getMatch() == otherTeam.getMatch();
                }
                
                case ALONE:{
                    return false;
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return true;
    }
}
