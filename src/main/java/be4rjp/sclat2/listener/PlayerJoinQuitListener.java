package be4rjp.sclat2.listener;

import be4rjp.sclat2.match.PlayerLobbyMatch;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.RegionBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
    
        PlayerLobbyMatch match = new PlayerLobbyMatch();
        SclatTeam sclatTeam = new SclatTeam(match, SclatColor.BLUE);
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        match.startBlockUpdate();
        sclatTeam.join(sclatPlayer);
    }
    
    
    @EventHandler
    public void onClick(PlayerAnimationEvent event){
        Player player = event.getPlayer();
        if(!player.isSneaking()) return;
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        
        Location loc = player.getLocation();
        RegionBlocks regionBlocks = new RegionBlocks(loc.clone().add(-10, -10, -10), loc.clone().add(10, 10, 10));
        regionBlocks.getBlocks().forEach(block -> sclatPlayer.getSclatTeam().getMatch().getBlockUpdater().setBlock(block, Material.BLUE_WOOL));
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
    }
}
