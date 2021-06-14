package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatScoreboard;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardScore;

import java.lang.reflect.Field;

public class ScoreUpdatePacketManager {
    
    private static Field a;
    
    static {
        try{
            a = PacketPlayOutScoreboardScore.class.getDeclaredField("a");
            a.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }
    
    
    public static boolean write(PacketPlayOutScoreboardScore scorePacket, SclatPlayer sclatPlayer){
        try {
            String line = (String) a.get(scorePacket);
            
            if(sclatPlayer.getScoreBoard() == null) return true;
            if(!line.contains("sclatSB")) return true;
            
            SclatScoreboard scoreboard = sclatPlayer.getScoreBoard();
            int index = Integer.parseInt(line.replace("sclatSB", ""));
            
            String newLine = scoreboard.getSidebarLine(sclatPlayer, index);
            if(newLine == null) return false;
            
            a.set(scorePacket, newLine);
            return true;
        }catch (Exception e){e.printStackTrace();}
        return true;
    }
    
}
