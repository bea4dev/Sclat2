package be4rjp.sclat2;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SclatConfig {
    
    private static MySQLConfig mySQLConfig;
    public static MySQLConfig getMySQLConfig(){return mySQLConfig;}
    
    
    public static void load(){
        File file = new File("plugins/Sclat2", "config.yml");
        file.getParentFile().mkdirs();
        
        if(!file.exists()){
            Sclat.getPlugin().saveResource("config.yml", false);
        }
        
        //ロードと値の保持
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        
        String host = yml.getString("my-sql.host");
        String port = yml.getString("my-sql.port");
        String database = yml.getString("my-sql.data-base");
        String table = yml.getString("my-sql.table");
        String user = yml.getString("my-sql.user");
        String password = yml.getString("my-sql.password");
        mySQLConfig = new MySQLConfig(host, port, database, table, user, password);
    }
    
    
    public static class MySQLConfig{
        public final String ip, port, database, table, username, password;
    
        public MySQLConfig(String ip, String port, String database, String table, String username, String password) {
            this.ip = ip;
            this.port = port;
            this.database = database;
            this.table = table;
            this.username = username;
            this.password = password;
        }
    }
}
