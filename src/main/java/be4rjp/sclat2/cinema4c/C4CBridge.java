package be4rjp.sclat2.cinema4c;

import be4rjp.cinema4c.bridge.PluginBridge;
import be4rjp.cinema4c.player.ScenePlayer;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.intro.IntroManager;
import be4rjp.sclat2.match.intro.SquidRunnable;
import be4rjp.sclat2.player.SclatPlayer;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntitySquid;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class C4CBridge implements PluginBridge {
    
    public static final Map<ScenePlayer, Set<UUID>> uuidMap = new ConcurrentHashMap<>();
    
    @Override
    public void executeCommand(ScenePlayer scenePlayer, String command) {
        String[] args = command.split(" ");
    
        if(args.length < 2) return;
        
        //intro-npc [team-number]
        switch (args[0]){
            case "intro-npc":{
                
                int teamNumber = Integer.parseInt(args[1]);
                Match match = IntroManager.getMatchByMoviePlayID(scenePlayer.getMoviePlayID());
                if(match == null) return;
    
                Set<UUID> uuids = new ConcurrentSet<>();
                for(SclatPlayer sclatPlayer : match.getSclatTeams().get(teamNumber).getTeamMembers()) {
                    UUID uuid = UUID.fromString(sclatPlayer.getUUID());
                    EntityPlayer npc = IntroManager.getNPC(uuid);
                    EntitySquid squid = IntroManager.getSquid(uuid);
                    if(npc == null || squid == null) continue;
                    
                    uuids.add(uuid);
                    new SquidRunnable(sclatPlayer, npc, squid).start();
                }
                uuidMap.put(scenePlayer, uuids);
                
                break;
            }
        }
    }
}
