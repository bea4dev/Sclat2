package be4rjp.sclat2.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RegionBlocks {
    private final World world;
    
    private final Vector maximum;
    private final Vector minimum;
    
    public RegionBlocks(Location firstPoint, Location secondPoint) {
        world = firstPoint.getWorld();
        Vector firstVector = firstPoint.toVector();
        Vector secondVector = secondPoint.toVector();
        maximum = Vector.getMaximum(firstVector, secondVector);
        minimum = Vector.getMinimum(firstVector, secondVector);
    }
    
    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
            for (int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
                for (int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        
        return blocks;
    }
}