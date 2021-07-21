package be4rjp.sclat2.weapon;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.weapon.main.MainWeapon;
import be4rjp.sclat2.weapon.special.Barrier;
import be4rjp.sclat2.weapon.sub.SplashBomb;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;

public class WeaponManager {
    
    public static SclatWeapon getSclatWeaponByItem(ItemStack itemStack){
        if(itemStack == null) return null;
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if(nmsItemStack.getTag() == null) return null;
    
        String id = nmsItemStack.getTag().getString("swid");
        if(id == null) return null;
        
        return SclatWeapon.getSclatWeapon(id);
    }
    
    public static MainWeapon getMainWeaponByItem(ItemStack itemStack){
        if(itemStack == null) return null;
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if(nmsItemStack.getTag() == null) return null;
    
        String id = nmsItemStack.getTag().getString("swid");
        if(id == null) return null;
        
        return MainWeapon.getMainWeapon(id);
    }
    
    
    public static void loadAllWeapon(){
        SclatWeapon.initialize();
        MainWeapon.initialize();
        Sclat.getPlugin().getLogger().info("Loading weapons...");
        File dir = new File("plugins/Sclat2/weapon");
    
        dir.getParentFile().mkdir();
        dir.mkdir();
        File[] files = dir.listFiles();
        if(files.length == 0){
            Sclat.getPlugin().saveResource("weapon/splat_shooter.yml", false);
            Sclat.getPlugin().saveResource("weapon/wakaba.yml", false);
            Sclat.getPlugin().saveResource("weapon/splat_charger.yml", false);
            Sclat.getPlugin().saveResource("weapon/splat_roller.yml", false);
            files = dir.listFiles();
        }
    
        if(files != null) {
            for (File file : files) {
                Sclat.getPlugin().getLogger().info(file.getName());
                String id = file.getName().replace(".yml", "");
                try {
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                    MainWeapon.MainWeaponType type = MainWeapon.MainWeaponType.valueOf(yml.getString("type"));
                    MainWeapon mainWeapon = type.createMainWeaponInstance(id);
                    mainWeapon.loadData(yml);
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }


    public static ItemStack writeNBTTag(SclatWeapon sclatWeapon, ItemStack itemStack){
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        Objects.requireNonNull(nmsItemStack.getTag()).setString("swid", sclatWeapon.getId());
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
    
    
    public static void setupSubWeapon(){
        new SplashBomb("SPLASH_BOMB");
    }
    
    public static void setupSPWeapon(){
        new Barrier("BARRIER");
    }
}
