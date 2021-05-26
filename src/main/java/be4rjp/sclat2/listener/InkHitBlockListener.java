package be4rjp.sclat2.listener;

import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.event.AsyncInkHitBlockEvent;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InkHitBlockListener implements Listener {
    @EventHandler
    public void onInkHit(AsyncInkHitBlockEvent event){
        InkBullet inkBullet = event.getInkBullet();
        SclatPlayer shooter = inkBullet.getShooter();
        Block hitBlock = event.getHitBlock();
        if(shooter == null) return;
        if(shooter.getSclatTeam() == null) return;
    
        SclatTeam team = shooter.getSclatTeam();
        team.getMatch().getBlockUpdater().setBlock(hitBlock, team.getSclatColor().getWool());
    }
}
