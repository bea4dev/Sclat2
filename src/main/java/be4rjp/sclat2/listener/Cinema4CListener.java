package be4rjp.sclat2.listener;

import be4rjp.cinema4c.event.AsyncMoviePlayFinishEvent;
import be4rjp.cinema4c.event.AsyncScenePlayFinishEvent;
import be4rjp.cinema4c.player.ScenePlayer;
import be4rjp.sclat2.cinema4c.C4CBridge;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.intro.IntroManager;
import be4rjp.sclat2.match.intro.ReadyRunnable;
import be4rjp.sclat2.player.SclatPlayer;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.util.Set;
import java.util.UUID;

public class Cinema4CListener implements Listener {

    @EventHandler
    public void onFinishMovie(AsyncMoviePlayFinishEvent event){
        int playID = event.getPlayID();

        Match match = IntroManager.getMatchByMoviePlayID(playID);
        if(match != null){
            ReadyRunnable readyRunnable = new ReadyRunnable(match);
            readyRunnable.start();
        }
    }
    
    
    @EventHandler
    public void onFinishScene(AsyncScenePlayFinishEvent event){
        ScenePlayer scenePlayer = event.getScenePlayer();
        Set<UUID> uuids = C4CBridge.uuidMap.get(scenePlayer);
        if(uuids == null) return;
        
        for(UUID uuid : uuids){
            EntityPlayer npc = IntroManager.getNPC(uuid);
    
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(npc.getId());
            PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);
            SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(uuid.toString());
            sclatPlayer.getSclatTeam().getMatch().getPlayers().forEach(sp -> {
                sp.sendPacket(destroy);
                sp.sendPacket(info);
            });
    
            Team team = sclatPlayer.getSclatTeam().getScoreBoardTeam();
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        }
    }
}
