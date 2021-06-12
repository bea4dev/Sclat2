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
    
    
    //マップ紹介ムービー
    private MovieData introMovie;
    //リザルト用ムービー
    private MovieData resultMovie;
    
    
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
        if(yml.contains("intro-movie")){
            String movieName = yml.getString("intro-movie");
            this.introMovie = PlayManager.getMovieData(movieName);
            if(introMovie == null) throw new IllegalArgumentException("No movie data with the name '" + movieName + "' was found.");
        }
    
        if(yml.contains("result-movie")){
            String movieName = yml.getString("result-movie");
            this.introMovie = PlayManager.getMovieData(movieName);
            if(introMovie == null) throw new IllegalArgumentException("No movie data with the name '" + movieName + "' was found.");
        }
    }
    
    /**
     * マップ紹介ムービーを取得する
     * @return MovieData
     */
    public MovieData getIntroMovie() {return introMovie;}
    
    /**
     * リザルト用ムービーを取得する
     * @return MovieData
     */
    public MovieData getResultMovie() {return resultMovie;}
}
