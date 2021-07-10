package be4rjp.sclat2.match;

import be4rjp.sclat2.match.map.SclatMap;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PaintManager {
    
    private static final Set<Material> NO_PAINT_BLOCKS;
    
    static {
        NO_PAINT_BLOCKS = new HashSet<>(Arrays.asList(Material.WATER, Material.OBSIDIAN, Material.IRON_BARS, Material.LAVA));
        Set<String> NO_PAINT_BLOCKS_END_WITH = new HashSet<>(Arrays.asList("GLASS", "GLASS_PANE", "DOOR", "STAIRS", "FENCE"));
        
        for(Material material : Material.values()){
            for(String end : NO_PAINT_BLOCKS_END_WITH) {
                if (material.toString().endsWith(end)){
                    NO_PAINT_BLOCKS.add(material);
                    break;
                }
            }
        }
    }
    
    public static boolean isCanPaint(Material material, SclatMap sclatMap){
        if(material == null || sclatMap == null) return false;
        return !(NO_PAINT_BLOCKS.contains(material) || sclatMap.getUnpaintableBlock().contains(material));
    }
}
