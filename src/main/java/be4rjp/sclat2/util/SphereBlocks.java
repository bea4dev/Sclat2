package be4rjp.sclat2.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Set;

public class SphereBlocks {
    //半径
    private final double radius;
    //中心座標
    private final Location center;
    
    /**
     * @param radius 半径
     * @param center 中心座標
     */
    public SphereBlocks(double radius, Location center){
        this.radius = radius;
        this.center = center;
    }
    
    /**
     * 中心座標から球状にブロックを取得する
     * @return
     */
    public Set<Block> getBlocks(){
        RegionBlocks regionBlocks = new RegionBlocks(center.clone().add(-radius, -radius, -radius), center.clone().add(radius, radius, radius));
        Set<Block> blocks = regionBlocks.getBlocks();
        double radiusSquare = radius * radius;
        blocks.removeIf(block -> block.getLocation().distanceSquared(center) > radiusSquare);
        blocks.removeIf(block -> block.getType().toString().endsWith("AIR"));
        return blocks;
    }
}
