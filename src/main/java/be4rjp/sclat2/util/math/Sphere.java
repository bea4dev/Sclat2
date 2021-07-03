package be4rjp.sclat2.util.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

/**
 *
 * @author Be4rJP
 */
public class Sphere {
    
    public static Set<Location> getSphere(Location baseLoc, double radius, int accuracy){
        Set<Location> tempList = new HashSet<>();
        for(int i = 0; i < 180; i += accuracy){
            for(int t = 0; t < 360; t += accuracy){
                double x = radius * Math.cos(Math.toRadians(i)) * Math.cos(Math.toRadians(t));
                double y = radius * Math.cos(Math.toRadians(i)) * Math.sin(Math.toRadians(t));
                double z = radius * Math.sin(Math.toRadians(i));
                Location sphereLoc = new Location(baseLoc.getWorld(), baseLoc.getX() + x, baseLoc.getY() + y, baseLoc.getZ() + z);
                Location sphereLoc_reflect = new Location(baseLoc.getWorld(), baseLoc.getX() - x, baseLoc.getY() - y, baseLoc.getZ() - z);
                tempList.add(sphereLoc);
                tempList.add(sphereLoc_reflect);
            }
        }
        return tempList;
    }
    
    public static List<Location> getXZCircle(Location baseLoc, double r, double r_accuracy ,int accuracy){
        List<Location> tempList = new ArrayList<Location>();
        for(int tr = 1; tr <= r; tr+=r_accuracy){
            for(int t = 0; t < 360; t += accuracy/tr){
                double x = tr * Math.sin(Math.toRadians(t));
                double z = tr * Math.cos(Math.toRadians(t));
                Location loc = new Location(baseLoc.getWorld(), baseLoc.getX() + x, baseLoc.getY(), baseLoc.getZ() + z);
                tempList.add(loc);
            }
        }
        return tempList;
    }
    
}
