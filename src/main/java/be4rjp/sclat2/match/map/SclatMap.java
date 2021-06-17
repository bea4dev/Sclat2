package be4rjp.sclat2.match.map;

import be4rjp.cinema4c.data.play.MovieData;
import be4rjp.cinema4c.player.PlayManager;
import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public abstract class SclatMap {
    
    //IDとSclatMapのハッシュマップ
    private static final Map<String, SclatMap> maps = new HashMap<>();
    
    public static void initialize(){
        maps.clear();
        NormalPVPMap.initialize();
    }
    
    /**
     * SclatMapを取得する
     * @param id
     * @return SclatMap
     */
    public static SclatMap getSclatMap(String id){return maps.get(id);}
    
    
    public static void loadAllSclatMap(){
        initialize();
        
        Sclat.getPlugin().getLogger().info("Loading maps...");
        File dir = new File("plugins/Sclat2/map");
    
        dir.getParentFile().mkdir();
        dir.mkdir();
        File[] files = dir.listFiles();
        if(files.length == 0){
            Sclat.getPlugin().saveResource("map/shionome.yml", false);
            files = dir.listFiles();
        }
    
        if(files != null) {
            for (File file : files) {
                Sclat.getPlugin().getLogger().info(file.getName());
                String id = file.getName().replace(".yml", "");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                MapType mapType = MapType.valueOf(yml.getString("type"));
                SclatMap sclatMap = mapType.createMapInstance(id);
                if(sclatMap == null) continue;
                
                sclatMap.loadData(yml);
            }
        }
    }
    
    
    //識別ID
    protected final String id;
    
    protected SclatMap(String id){
        this.id = id;
        maps.put(id, this);
    }
    
    public String getID() {return this.id;}
    
    //設定ファイル
    protected YamlConfiguration yml;
    //表示名
    protected HashMap<Lang, String> displayName = new HashMap<>();
    //待機場所
    protected Location waitLocation;
    //チームのスポーン場所
    protected final List<Location> teamLocations = new ArrayList<>();
    //塗れないブロック
    protected Set<Material> unpaintableBlock = new HashSet<>();
    //マップ紹介ムービー
    protected MovieData introMovie = null;
    //リザルト用ムービー
    protected MovieData resultMovie = null;
    
    
    /**
     * 表示名を取得する
     * @return String
     */
    public String getDisplayName(Lang lang) {
        String name = displayName.get(lang);
        if(name == null){
            return "No name.";
        }else{
            return name;
        }
    }
    
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
    
    /**
     * 設定されているチームのスポーン場所の数を取得します
     * @return
     */
    public int getNumberOfTeamLocations(){return this.teamLocations.size();}
    
    /**
     * このマップのタイプを取得する
     * @return
     */
    public abstract MapType getType();
    
    
    /**
     * 設定ファイルからロードする
     * @param yml
     */
    public void loadData(YamlConfiguration yml){
        this.yml = yml;
    
        if(yml.contains("display-name")){
            for(String languageName : yml.getConfigurationSection("display-name").getKeys(false)){
                Lang lang = Lang.valueOf(languageName);
                String name = yml.getString("display-name." + languageName);
                this.displayName.put(lang, ChatColor.translateAlternateColorCodes('&', name));
            }
        }
        
        if(yml.contains("wait-location")) this.waitLocation = ConfigUtil.getLocationByString(yml.getString("wait-location"));
        
        if(yml.contains("team-spawn-locations")){
            for(String locString : yml.getStringList("team-spawn-locations")){
                this.teamLocations.add(ConfigUtil.getLocationByString(locString).add(0.5, 0.0, 0.5));
            }
        }

        if(yml.contains("intro-movie")){
            String movieName = yml.getString("intro-movie");
            this.introMovie = PlayManager.getMovieData(movieName);
            if(introMovie == null) throw new IllegalArgumentException("No movie data with the name '" + movieName + "' was found.");
        }

        if(yml.contains("result-movie")){
            String movieName = yml.getString("result-movie");
            this.resultMovie = PlayManager.getMovieData(movieName);
            if(resultMovie == null) throw new IllegalArgumentException("No movie data with the name '" + movieName + "' was found.");
        }
        
        loadDetailsData();
    }
    
    
    /**
     * 各マップの詳細データを取得する
     */
    public abstract void loadDetailsData();

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


    
    public enum MapType{
        NORMAL_PVP(NormalPVPMap.class);
        
        private final Class<? extends SclatMap> clazz;
        
        MapType(Class<? extends SclatMap> clazz){
            this.clazz = clazz;
        }
        
        public SclatMap createMapInstance(String id){
            try{
                return clazz.getConstructor(String.class).newInstance(id);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
    }
}
