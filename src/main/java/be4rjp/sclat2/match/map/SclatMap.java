package be4rjp.sclat2.match.map;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public abstract class SclatMap {
    
    private static final Map<String, SclatMap> maps = new HashMap<>();
    
    public static SclatMap getSclatMap(String id){return maps.get(id);}
    
    
    
    //識別ID
    protected final String id;
    
    protected SclatMap(String id){
        this.id = id;
        maps.put(id, this);
    }
    
    public String getID() {return this.id;}
    
    //表示名
    protected String displayName;
    //待機場所
    protected Location waitLocation;
    //チームのスポーン場所
    protected final List<Location> teamLocations = new ArrayList<>();
    //塗れないブロック
    protected Set<Material> unpaintableBlock = new HashSet<>();
    
    /**
     * 試合の待機場所を取得する
     * @return Location
     */
    public Location getWaitLocation(){return this.waitLocation;}
    
    /**
     * チームのスポーン場所を取得する
     * @param teamNumber チームの番号
     * @return Location
     */
    public Location getTeamLocation(int teamNumber){return this.teamLocations.get(teamNumber);}
    
    
    
    
    
    public enum MapType{
        NORMAL_PVP(SclatMap.class);
        
        private final Class<? extends SclatMap> clazz;
        
        MapType(Class<? extends SclatMap> clazz){
            this.clazz = clazz;
        }
    }
}
