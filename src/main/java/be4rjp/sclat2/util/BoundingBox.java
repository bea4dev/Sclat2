package be4rjp.sclat2.util;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;

public class BoundingBox {
    
    Vector max;
    Vector min;
    
    public BoundingBox(Vector min, Vector max) {
        this.max = max;
        this.min = min;
    }
    
    
    public BoundingBox(Entity entity){
        AxisAlignedBB bb = ((CraftEntity) entity).getHandle().getBoundingBox();
        min = new Vector(bb.minX - 0.15, bb.minY, bb.minZ - 0.15);
        max = new Vector(bb.maxX + 0.15, bb.maxY, bb.maxZ + 0.15);
    }
    
    public BoundingBox (AxisAlignedBB bb){
        min = new Vector(bb.minX, bb.minY, bb.minZ);
        max = new Vector(bb.maxX, bb.maxY, bb.maxZ);
    }
    
    public Vector midPoint(){
        return max.clone().add(min).multiply(0.5);
    }
    
}
