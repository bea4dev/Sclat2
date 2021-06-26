package be4rjp.sclat2.weapon;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.main.Charger;
import be4rjp.sclat2.weapon.main.FixedRateShooter;
import be4rjp.sclat2.weapon.main.Shooter;
import be4rjp.sclat2.weapon.main.runnable.MainWeaponRunnable;
import be4rjp.sclat2.weapon.main.runnable.ShooterRunnable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MainWeapon extends SclatWeapon{
    
    //識別IDとメインウエポンのマップ
    private static Map<String, MainWeapon> mainWeaponMap = new ConcurrentHashMap<>();
    
    public static void initialize(){
        mainWeaponMap.clear();
    }
    
    /**
     * 識別IDからメインウエポンを取得します
     * @param id 識別ID
     * @return MainWeapon
     */
    public static MainWeapon getMainWeapon(String id){return mainWeaponMap.get(id);}
    
    /**
     * メインウエポンのリストを取得します
     * @return Collection<MainWeapon>
     */
    public static Collection<MainWeapon> getMainWeaponList(){return mainWeaponMap.values();}
    
    
    
    //武器の識別名
    protected final String id;
    //設定ファイル
    protected YamlConfiguration yml;
    //武器のマテリアル
    protected Material material = Material.BARRIER;
    //CustomModelDataのID
    protected int modelID = 0;
    //一発分の塗り半径
    protected double paintRadius = 0.0;
    //塗った時にパーティクルを表示するかどうか
    protected boolean particle = false;
    //射撃時に鳴らすサウンド
    protected SclatSound shootSound = new SclatSound(Sound.ENTITY_PIG_STEP, 0.3F, 1F);
    
    public MainWeapon(String id){
        this.id = id;
        mainWeaponMap.put(id, this);
        SclatWeapon.registerSclatWeapon(id, this);
    }
    
    /**
     * ItemStackを取得する
     * @return ItemStack
     */
    public ItemStack getItemStack(Lang lang){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getDisplayName(lang));
        itemMeta.setCustomModelData(modelID);
        itemStack.setItemMeta(itemMeta);
    
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        Objects.requireNonNull(nmsItemStack.getTag()).setString("swid", id);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
    
    /**
     * 識別名を取得する
     * @return String
     */
    public String getID() {return id;}
    
    /**
     * マテリアルを取得する
     * @return Material
     */
    public Material getMaterial() {return material;}
    
    /**
     * CustomModelDataのIDを取得する
     * @return int
     */
    public int getModelID() {return modelID;}
    
    /**
     * 一発分の塗り半径を取得する
     * @return double
     */
    public double getPaintRadius() {return paintRadius;}
    
    /**
     * 射撃時に鳴らすサウンドを取得する
     * @return SclatSound
     */
    public SclatSound getShootSound() {return shootSound;}
    
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer){
        //None
    }
    
    @Override
    public abstract void onRightClick(SclatPlayer sclatPlayer);
    
    
    /**
     * 武器のタイプを取得する
     * @return MainWeaponType
     */
    public abstract MainWeaponType getType();
    
    
    /**
     * ymlファイルからロードする
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
        if(yml.contains("material")) this.material = Material.getMaterial(Objects.requireNonNull(yml.getString("material")));
        if(yml.contains("custom-model-data")) this.modelID = yml.getInt("custom-model-data");
        if(yml.contains("paint-radius")) this.paintRadius = yml.getDouble("paint-radius");
        if(yml.contains("need-ink")) this.needInk = (float)yml.getDouble("need-ink");
        if(yml.contains("damage")) this.damage = (float)yml.getDouble("damage");
        if(yml.contains("particle")) this.particle = yml.getBoolean("particle");
        if(yml.contains("sound")){
            String[] args = yml.getString("sound").split("/");
            Sound sound = Sound.valueOf(args[0]);
            float volume = Float.parseFloat(args[1]);
            float pitch = Float.parseFloat(args[2]);
            SclatSound sclatSound = new SclatSound(sound, volume, pitch);
            this.shootSound = sclatSound;
        }
        
        loadDetailsData();
    }
    
    /**
     * ymlファイルから詳細なデータをロードする
     */
    public abstract void loadDetailsData();
    
    
    
    public enum MainWeaponType{
        SHOOTER(Shooter.class),
        FIXED_RATE_SHOOTER(FixedRateShooter.class),
        CHARGER(Charger.class);
        
        private final Class<? extends MainWeapon> weaponClass;
        
        MainWeaponType(Class<? extends MainWeapon> weaponClass){
            this.weaponClass = weaponClass;
        }
        
        public MainWeapon createMainWeaponInstance(String id){
            try{
                return weaponClass.getConstructor(String.class).newInstance(id);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
    }
}
