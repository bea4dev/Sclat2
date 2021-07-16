package be4rjp.sclat2.match.intro;

import be4rjp.cinema4c.data.play.MovieData;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.ObservableOption;
import be4rjp.sclat2.player.SclatPlayer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IntroManager {

    private static Map<Integer, Match> matchMap = new ConcurrentHashMap<>();
    //イントロのNPCのマップ
    private static Map<UUID, EntityPlayer> npcMap = new ConcurrentHashMap<>();
    //イントロのイカのマップ
    private static Map<UUID, EntitySquid> squidMap = new ConcurrentHashMap<>();

    public static Match getMatchByMoviePlayID(int playID){return matchMap.get(playID);}

    private static void registerMatch(Match match, int playID){matchMap.put(playID, match);}
    
    public static EntityPlayer getNPC(UUID uuid){return npcMap.get(uuid);}
    
    public static EntitySquid getSquid(UUID uuid){return squidMap.get(uuid);}


    public static void playIntro(Match match){
        SclatMap sclatMap = match.getSclatMap();
        MovieData movieData = sclatMap.getIntroMovie();
        if(movieData == null) return;

        Set<Player> players = new HashSet<>();
        match.getPlayers().stream()
                .filter(sclatPlayer -> sclatPlayer.getBukkitPlayer() != null)
                .forEach(sclatPlayer -> players.add(sclatPlayer.getBukkitPlayer()));
    
        match.setPlayerObservableOption(ObservableOption.ALONE);
        
        int playID = movieData.play(players);
        initializeNPC(match);
        registerMatch(match, playID);
        new MapIntroRunnable(match).start();
    }
    
    
    public static void initializeNPC(Match match){
        int index = 0;
        for(SclatTeam sclatTeam : match.getSclatTeams()){
            Location teamLocation = match.getSclatMap().getTeamLocation(index);
    
            Team team = sclatTeam.getScoreBoardTeam();
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(false);
            
            int playerIndex = 0;
            for(SclatPlayer sclatPlayer : sclatTeam.getTeamMembers()){
                Location playerLocation = teamLocation.clone();
                switch (playerIndex){
                    case 0:{
                        playerLocation.add(1, 0, 1);
                        break;
                    }
                    case 1:{
                        playerLocation.add(-1, 0, 1);
                        break;
                    }
                    case 2:{
                        playerLocation.add(1, 0, -1);
                        break;
                    }
                    case 3:{
                        playerLocation.add(-1, 0, -1);
                        break;
                    }
                }
                
                Player player = sclatPlayer.getBukkitPlayer();
                if(player == null) continue;
    
                MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
                WorldServer nmsWorld = ((CraftWorld) playerLocation.getWorld()).getHandle();
                
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
                String[] skin = sclatPlayer.getSkin();
                if(skin != null) {
                    gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
                }
    
                EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
                npc.setLocation(playerLocation.getX(), playerLocation.getY() - 3.5, playerLocation.getZ(), playerLocation.getYaw(), 0);
                npc.setInvisible(true);
                npc.getDataWatcher().set( DataWatcherRegistry.a.a(16), (byte)127);
                PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
                PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(npc);
                PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true);
                PacketPlayOutEntityHeadRotation rotation = new PacketPlayOutEntityHeadRotation(npc, (byte) ((playerLocation.getYaw() * 256.0F) / 360.0F));
                
                EntitySquid squid = new EntitySquid(EntityTypes.SQUID, nmsWorld);
                squid.setLocation(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), 0);
                squid.setCustomName(CraftChatMessage.fromStringOrNull(sclatPlayer.getDisplayName()));
                squid.setCustomNameVisible(true);
                PacketPlayOutSpawnEntityLiving squidSpawn = new PacketPlayOutSpawnEntityLiving(squid);
                PacketPlayOutEntityMetadata squidMetadata = new PacketPlayOutEntityMetadata(squid.getId(), squid.getDataWatcher(), true);
                
                for(SclatPlayer sp : match.getPlayers()){
                    sp.sendPacket(info);
                    sp.sendPacket(spawn);
                    sp.sendPacket(metadata);
                    sp.sendPacket(rotation);
                    
                    sp.sendPacket(squidSpawn);
                    sp.sendPacket(squidMetadata);
                }
                
                npcMap.put(player.getUniqueId(), npc);
                squidMap.put(player.getUniqueId(), squid);
                
                playerIndex++;
                if(playerIndex == 4){
                    break;
                }
            }
            
            
            index++;
        }
    }
}
