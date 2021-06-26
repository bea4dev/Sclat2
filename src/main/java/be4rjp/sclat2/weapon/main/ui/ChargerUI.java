package be4rjp.sclat2.weapon.main.ui;

import be4rjp.sclat2.Sclat;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargerUI {
    
    //IDとスコープのマップ
    private static final Map<String, ChargerUI> uiMap = new HashMap<>();
    
    public static void initialize(){
        uiMap.clear();
    }
    
    /**
     * スコープを取得する
     * @param id スコープのID
     * @return ChargerUI
     */
    public static ChargerUI getChargerUI(String id){return uiMap.get(id);}
    
    
    public static void loadAllUI() {
        initialize();
    
        Sclat.getPlugin().getLogger().info("Loading ui...");
        File dir = new File("plugins/Sclat2/ui");
    
        dir.getParentFile().mkdir();
        dir.mkdir();
        File[] files = dir.listFiles();
        if (files.length == 0) {
            Sclat.getPlugin().saveResource("ui/charger_x3.yml", false);
            files = dir.listFiles();
        }
    
        if (files != null) {
            for (File file : files) {
                Sclat.getPlugin().getLogger().info(file.getName());
                String id = file.getName().replace(".yml", "");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                ChargerUI chargerUI = new ChargerUI(id);
                chargerUI.loadData(yml);
                uiMap.put(id, chargerUI);
            }
        }
    }
    
    
    
    
    private final String id;
    private final List<ChargerUIInfo> infoList;
    
    public ChargerUI(String id){
        this.id = id;
        
        this.infoList = new ArrayList<>();
    }
    
    public void loadData(YamlConfiguration yml){
        if(!yml.contains("scope")) return;
        
        for(String line : yml.getStringList("scope")){
            line = line.replace(" ", "");
            String[] args = line.split(",");
            
            float fov = Float.parseFloat(args[0]);
    
            int[] charArray = null;
            /*
            if(!args[1].equals("")) {
                String titleUnicode = args[1];
                charArray = new int[]{Integer.parseInt(titleUnicode, 16)};
            }*/
    
            int[] charArray2 = null;
            /*
            if(!args[2].equals("")) {
                String subtitleUnicode = args[2];
                charArray2 = new int[]{Integer.parseInt(subtitleUnicode, 16)};
            }*/
            
            ChargerUIInfo info = new ChargerUIInfo(fov, charArray != null ? new String(charArray, 0, charArray.length) : "", charArray2 != null ? new String(charArray2, 0, charArray2.length) : "");
            infoList.add(info);
        }
    }
    
    
    public ChargerUIInfo getInfo(int tick){
        if(infoList.size() <= tick) return null;
        
        return infoList.get(tick);
    }
    
    
    
    public static class ChargerUIInfo{
        
        public final float fov;
        public final String title;
        public final String subtitle;
        
        public ChargerUIInfo(float fov, String title, String subtitle){
            this.fov = fov;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
