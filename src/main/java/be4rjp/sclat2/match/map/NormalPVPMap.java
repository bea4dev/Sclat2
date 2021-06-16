package be4rjp.sclat2.match.map;

import be4rjp.cinema4c.data.play.MovieData;
import be4rjp.cinema4c.player.PlayManager;

import java.util.*;

public class NormalPVPMap extends SclatMap{
    
    //全てのPVPマップのリスト
    private static List<NormalPVPMap> normalPVPMaps = new ArrayList<>();
    
    /**
     * ランダムにPVPマップを取得する
     * @return
     */
    public static NormalPVPMap getRandomPVPMap(){return normalPVPMaps.get(new Random().nextInt(normalPVPMaps.size()));}
    
    public static void initialize(){normalPVPMaps.clear();}
    
    
    public NormalPVPMap(String id) {
        super(id);
        normalPVPMaps.add(this);
    }
    
    @Override
    public MapType getType() {
        return MapType.NORMAL_PVP;
    }
    
    @Override
    public void loadDetailsData() {

    }
}
