package be4rjp.sclat2.player.costume;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HeadGear {
    
    private static final Map<String, HeadGear> idMap = new HashMap<>();
    private static final Map<Integer, HeadGear> saveNumberMap = new HashMap<>();
    
    public static HeadGear getHeadGear(String id){return idMap.get(id);}
    
    public static HeadGear getHeadGearBySaveNumber(int saveNumber){return saveNumberMap.get(saveNumber);}
    
    
    public static void loadAllHeadGear() {
        Sclat.getPlugin().getLogger().info("Loading head gears...");
        File dir = new File("plugins/Sclat2/gear");
    
        dir.getParentFile().mkdir();
        dir.mkdir();
        File[] files = dir.listFiles();
        if (files.length == 0) {
            Sclat.getPlugin().saveResource("gear/helmet.yml", false);
            files = dir.listFiles();
        }
    
        if (files != null) {
            for (File file : files) {
                Sclat.getPlugin().getLogger().info(file.getName());
                String id = file.getName().replace(".yml", "");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                HeadGear headGear = new HeadGear(id);
                headGear.loadData(yml);
            }
        }
    }
    
    
    //識別用ID
    private final String id;
    //設定ファイル
    private YamlConfiguration yml;
    //武器の表示名
    protected Map<Lang, String> displayName = new HashMap<>();
    //ヘッドギアのマテリアル
    protected Material material = Material.BARRIER;
    //CustomModelDataのID
    protected int modelID = 0;
    //ヘッドギアのセーブ & ロード時の識別番号 (1 ~ 65535)
    private int saveNumber = 0;
    
    
    public HeadGear(String id){
        this.id = id;
        idMap.put(id, this);
    }
    
    
    /**
     * ymlファイルからロードする
     * @param yml
     */
    public void loadData(YamlConfiguration yml) {
        this.yml = yml;
    
        if(yml.contains("display-name")){
            for(String languageName : yml.getConfigurationSection("display-name").getKeys(false)){
                Lang lang = Lang.valueOf(languageName);
                String name = yml.getString("display-name." + languageName);
                this.displayName.put(lang, ChatColor.translateAlternateColorCodes('&', name));
            }
        }
        if(yml.contains("material")) this.material = Material.getMaterial(Objects.requireNonNull(yml.getString("material")));
        if(yml.contains("custom-model-data")) this.modelID = yml.getInt("custom-model-data");
        if(yml.contains("save-number")){
            this.saveNumber = yml.getInt("save-number");
            saveNumberMap.put(saveNumber, this);
        }
    }
    
    public Material getMaterial() {return material;}
    
    public int getModelID() {return modelID;}
    
    public int getSaveNumber() {return saveNumber;}
    
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
    
    
    public ItemStack getItemStack(Lang lang){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getDisplayName(lang));
        itemMeta.setCustomModelData(modelID);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
