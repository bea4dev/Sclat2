package be4rjp.sclat2.weapon;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.special.SpecialWeapon;
import be4rjp.sclat2.weapon.sub.SubWeapon;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WeaponClass {
    
    private static Map<String, WeaponClass> weaponClassMap = new HashMap<>();
    private static Map<Integer, WeaponClass> saveNumberMap = new HashMap<>();
    
    public static WeaponClass getWeaponClass(String id){return weaponClassMap.get(id);}
    
    public static WeaponClass getWeaponClassBySaveNumber(int saveNumber){return saveNumberMap.get(saveNumber);}
    
    
    public static void loadAllClass(){
        Sclat.getPlugin().getLogger().info("Loading classes...");
        File dir = new File("plugins/Sclat2/class");
    
        dir.getParentFile().mkdir();
        dir.mkdir();
        File[] files = dir.listFiles();
        if(files.length == 0){
            Sclat.getPlugin().saveResource("class/wakaba.yml", false);
            files = dir.listFiles();
        }
    
        if(files != null) {
            for (File file : files) {
                Sclat.getPlugin().getLogger().info(file.getName());
                String id = file.getName().replace(".yml", "");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                WeaponClass weaponClass = new WeaponClass(id);
                weaponClass.loadData(yml);
            }
        }
    }
    
    
    //識別用ID
    private String id;
    //設定ファイル
    private YamlConfiguration yml;
    //メインウエポン
    private MainWeapon mainWeapon;
    //サブウエポン
    private SubWeapon subWeapon;
    //スペシャルウエポン
    private SpecialWeapon specialWeapon;
    //武器クラスのセーブ & ロード時の識別番号
    private int saveNumber = -1;
    //スペシャルウエポンのチャージに必要な塗りポイント
    private int spWeaponNeedPoint = 200;
    
    
    public WeaponClass(String id){
        this.id = id;
        weaponClassMap.put(id, this);
    }
    
    
    /**
     * ymlファイルからロードする
     * @param yml
     */
    public void loadData(YamlConfiguration yml) {
        this.yml = yml;
        
        if(yml.contains("main-weapon")) this.mainWeapon = MainWeapon.getMainWeapon(yml.getString("main-weapon"));
        if(yml.contains("sub-weapon")) this.subWeapon = (SubWeapon) SclatWeapon.getSclatWeapon(yml.getString("sub-weapon"));
        if(yml.contains("special-weapon")) this.specialWeapon = (SpecialWeapon) SclatWeapon.getSclatWeapon(yml.getString("special-weapon"));
        if(yml.contains("save-number")) this.saveNumber = yml.getInt("save-number");
        if(yml.contains("sp-weapon-need-point")) this.spWeaponNeedPoint = yml.getInt("sp-weapon-need-point");
        
        if(saveNumber != -1) saveNumberMap.put(saveNumber, this);
    }
    
    
    public void setWeaponClass(SclatPlayer sclatPlayer){
        Player player = sclatPlayer.getBukkitPlayer();
        if(player == null) return;
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam == null) return;
        
        player.getInventory().clear();
        Lang lang = sclatPlayer.getLang();
        if(mainWeapon != null) player.getInventory().setItem(0, mainWeapon.getItemStack(lang));
        if(subWeapon != null) player.getInventory().setItem(2, subWeapon.getItemStack(sclatTeam, lang));
        if(specialWeapon != null) player.getInventory().setItem(4, specialWeapon.getItemStack(lang));
    }
    
    public MainWeapon getMainWeapon() {return mainWeapon;}
    
    public SubWeapon getSubWeapon() {return subWeapon;}
    
    public SpecialWeapon getSpecialWeapon() {return specialWeapon;}
    
    public int getSaveNumber() {return saveNumber;}

    public int getSPWeaponNeedPoint() {return spWeaponNeedPoint;}
}
