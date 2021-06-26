package be4rjp.sclat2.match.intro;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.particle.BlockParticle;
import be4rjp.sclat2.util.particle.SclatParticle;
import be4rjp.sclat2.util.SclatSound;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class SquidRunnable extends BukkitRunnable {
    
    private static final SclatSound SQUID_SWIM = new SclatSound(Sound.ENTITY_PLAYER_SWIM, 1, 1);
    
    private SclatParticle SQUID_INK_PARTICLE;
    
    private final SclatPlayer sclatPlayer;
    private final SclatTeam team;
    private final Match match;
    private final EntityPlayer npc;
    private final EntitySquid squid;
    private final Location location;
    private int count = 0;
    
    public SquidRunnable(SclatPlayer sclatPlayer, EntityPlayer npc, EntitySquid squid){
        this.sclatPlayer = sclatPlayer;
        this.team = sclatPlayer.getSclatTeam();
        this.match = team.getMatch();
        this.npc = npc;
        this.squid = squid;
        
        Vec3D position = npc.getPositionVector();
        this.location = new Location(npc.getWorldServer().getWorld(), position.x, position.y + 3.5, position.z, npc.getBukkitYaw(), 0);
        this.SQUID_INK_PARTICLE = new BlockParticle(Particle.BLOCK_DUST, 10, 0.2, 0.2, 0.2, 1, team.getSclatColor().getWool().createBlockData());
    }
    
    
    @Override
    public void run() {
        
        if(count >= 0 && count <= 7){
            match.getPlayers().forEach(sclatPlayer -> sclatPlayer.spawnParticle(SQUID_INK_PARTICLE, location.clone().add(0.0, 0.25, 0.0)));
        }
        
        if(count == 7){
            npc.setInvisible(false);
            npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            
            PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(npc);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true);
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(squid.getId());
            PacketPlayOutAnimation animation = new PacketPlayOutAnimation(npc, 0);
            for(SclatPlayer sclatPlayer : match.getPlayers()){
                sclatPlayer.sendPacket(teleport);
                sclatPlayer.sendPacket(metadata);
                sclatPlayer.sendPacket(destroy);
                sclatPlayer.sendPacket(animation);
                sclatPlayer.playSound(SQUID_SWIM, location);
            }
            Team scoreboardTeam = team.getScoreBoardTeam();
            scoreboardTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            scoreboardTeam.setCanSeeFriendlyInvisibles(true);
            this.cancel();
        }
        
        count++;
    }
    
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 2);
    }
}
