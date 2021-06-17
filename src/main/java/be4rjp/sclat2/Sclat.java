package be4rjp.sclat2;

import be4rjp.sclat2.cinema4c.BridgeManager;
import be4rjp.sclat2.listener.*;
import be4rjp.sclat2.match.PlayerLobbyMatch;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.message.MessageManager;
import be4rjp.sclat2.weapon.WeaponManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Sclat extends JavaPlugin {
    
    private static Sclat plugin;
    //ロビー用の試合インスタンス
    private static PlayerLobbyMatch lobbyMatch;
    
    public static String VERSION = "v0.0.1 - α";
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
    
        MessageManager.loadAllMessage();
        SclatMap.loadAllSclatMap();
    
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);
        pluginManager.registerEvents(new InkHitBlockListener(), this);
        pluginManager.registerEvents(new PlayerItemClickListener(), this);
        pluginManager.registerEvents(new InkHitPlayerListener(), this);
        pluginManager.registerEvents(new Cinema4CListener(), this);
    
        WeaponManager.loadAllWeapon();

        lobbyMatch = new PlayerLobbyMatch(null);
        
        //For cinema4c extensions
        if(getServer().getPluginManager().getPlugin("Cinema4C") != null){
            getLogger().info("Registering cinema4c extensions...");
            BridgeManager.registerPluginBridge();
        }
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    
    public static Sclat getPlugin(){return plugin;}

    /**
     * ロビー用の試合インスタンスを取得する
     * @return PlayerLobbyMatch
     */
    public static PlayerLobbyMatch getLobbyMatch(){return lobbyMatch;}
}
