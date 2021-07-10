package be4rjp.sclat2.match;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchWaitRunnable;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.SclatScoreboard;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MatchManager {
    
    //名前とMatchManagerのマップ
    private static Map<String, MatchManager> matchManagerMap = new HashMap<>();
    
    /**
     * 名前からMatchManagerを取得する
     * @param name MatchManagerの名前
     * @return MatchManager
     */
    public static MatchManager getMatchManager(String name){return matchManagerMap.get(name);}
    
    
    public static void load() {
        File file = new File("plugins/Sclat2", "match-manager.yml");
        file.getParentFile().mkdirs();
    
        if (!file.exists()) {
            Sclat.getPlugin().saveResource("match-manager.yml", false);
        }
    
        //ロードと値の保持
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for(String name : Objects.requireNonNull(yml.getConfigurationSection("match-manager")).getKeys(false)){
            String displayName = yml.getString("match-manager." + name + ".display-name");
            MatchManageType type = MatchManageType.valueOf(yml.getString("match-manager." + name + ".type"));
            int minPlayer = yml.getInt("match-manager." + name + ".min-player");
            
            new MatchManager(name, displayName, type, minPlayer);
        }
    }
    
    
    
    private final String name;
    private final String displayName;
    private final MatchManageType type;
    private final int minPlayer;
    private final Set<SclatPlayer> joinedPlayers = new ConcurrentSet<>();
    private final SclatScoreboard scoreboard = new SclatScoreboard("§6§lSclat2§r " + Sclat.VERSION, 10);
    
    private Match match = null;
    private MatchWaitRunnable waitRunnable = null;
    
    public MatchManager(String name, String displayName, MatchManageType type, int minPlayer){
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.minPlayer = minPlayer;
        
        matchManagerMap.put(name, this);
    }
    
    
    public void join(SclatPlayer sclatPlayer){
        if(match == null) createMatch();
        if(match.getMatchStatus() == Match.MatchStatus.FINISHED) createMatch();
        
        sclatPlayer.teleport(match.getSclatMap().getWaitLocation());
        joinedPlayers.add(sclatPlayer);
        sclatPlayer.setMatchManager(this);
    }
    
    private void createMatch(){
        switch (type){
            case NAWABARI:{
                match = new NawabariMatch(SclatMap.getRandomMap());
                SclatColor[] colors = SclatColor.getRandomColorPair();
                new SclatTeam(match, colors[0]);
                new SclatTeam(match, colors[1]);
                match.initialize();
                break;
            }
        }
        
        waitRunnable = new MatchWaitRunnable(this, match, minPlayer);
        waitRunnable.start();
    }
    
    public Set<SclatPlayer> getJoinedPlayers() {return joinedPlayers;}
    
    public SclatScoreboard getScoreboard() {return scoreboard;}
    
    public MatchManageType getType() {return type;}
    
    public enum MatchManageType{
        NAWABARI
    }
}
