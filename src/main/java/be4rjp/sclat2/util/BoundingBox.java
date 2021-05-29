package be4rjp.sclat2.util;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
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
    
    
    public BoundingBox(Entity entity, double plus){
        AxisAlignedBB bb = ((CraftEntity) entity).getHandle().getBoundingBox();
        min = new Vector(bb.minX - plus, bb.minY, bb.minZ - plus);
        max = new Vector(bb.maxX + plus, bb.maxY, bb.maxZ + plus);
    }
    
    public BoundingBox (AxisAlignedBB bb){
        min = new Vector(bb.minX, bb.minY, bb.minZ);
        max = new Vector(bb.maxX, bb.maxY, bb.maxZ);
    }
    
    public Vector midPoint(){
        return max.clone().add(min).multiply(0.5);
    }
    
}
