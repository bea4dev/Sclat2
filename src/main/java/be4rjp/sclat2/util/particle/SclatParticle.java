package be4rjp.sclat2.util.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public abstract class SclatParticle {
    
    protected static double PARTICLE_DRAW_DISTANCE_SQUARE = 800;
    
    
    protected final Particle particle;
    protected final int count;
    protected final double x_offset;
    protected final double y_offset;
    protected final double z_offset;
    protected final double extra;
    
    protected SclatParticle(Particle particle, int count, double x_offset, double y_offset, double z_offset, double extra) {
        this.particle = particle;
        this.count = count;
        this.x_offset = x_offset;
        this.y_offset = y_offset;
        this.z_offset = z_offset;
        this.extra = extra;
    }
    
    
    public abstract void spawn(Player player, Location location);
}
