package be4rjp.sclat2.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class BlockParticle extends SclatParticle{
    
    private final BlockData blockData;
    
    public BlockParticle(Particle particle, int count, double x_offset, double y_offset, double z_offset, double extra, BlockData blockData) {
        super(particle, count, x_offset, y_offset, z_offset, extra);
        this.blockData = blockData;
    }
    
    @Override
    public void spawn(Player player, Location location) {
        if(player.getLocation().distanceSquared(location) > PARTICLE_DRAW_DISTANCE_SQUARE) return;
        player.spawnParticle(particle, location, count, x_offset, y_offset, z_offset, extra, blockData);
    }
}
