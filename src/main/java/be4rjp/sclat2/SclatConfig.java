package be4rjp.sclat2;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SclatConfig {
    
    
    public static void load(){
        File file = new File("plugins/Sclat2", "config.yml");
        file.getParentFile().mkdirs();
        
        if(!file.exists()){
            Sclat.getPlugin().saveResource("config.yml", false);
        }
        
        //ロードと値の保持
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        
    }
    
    
}
