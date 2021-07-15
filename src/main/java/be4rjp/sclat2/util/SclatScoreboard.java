package be4rjp.sclat2.util;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.player.SclatPlayer;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_15_R1.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SclatScoreboard {
    
    //Bukkitのスコアボード
    private final Scoreboard scoreboard;
    //サイドバーのオブジェクト
    private final Objective objective;
    //プレイヤーと画面右スコアラインのマップ
    private final Map<SclatPlayer, List<String>> playerLineMap = new ConcurrentHashMap<>();
    //変更を加えたラインのマップ
    private final Map<SclatPlayer, Set<String>> playerRemoveLineMap = new ConcurrentHashMap<>();
    
    private final int sidebarSize;
    
    /**
     * スコアボードを作成
     * @param displayName 表示名
     * @param sidebarSize サイドバーの行数
     */
    public SclatScoreboard(String displayName, int sidebarSize){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.sidebarSize = sidebarSize;
        this.scoreboard = scoreboardManager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sclatSB", "sclatSB", displayName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    
        List<String> lines = new ArrayList<>();
        for(int index = 0; index < sidebarSize; index++){
            lines.add("sclatSB" + index);
        }
        ObjectiveUtil.setLine(objective, lines);
    }
    
    
    /**
     * Bukkitのスコアボードを取得する
     * @return Scoreboard
     */
    public Scoreboard getBukkitScoreboard(){return this.scoreboard;}
    
    
    /**
     * プレイヤーごとにサイドバーのラインの文字列を取得します
     * @param sclatPlayer 取得したいプレイヤー
     * @param index 取得したいインデックス
     * @return String
     */
    public synchronized String getSidebarLine(SclatPlayer sclatPlayer, int index){
        List<String> lines = playerLineMap.get(sclatPlayer);
        if(lines == null) return null;
        if(lines.size() <= index) return null;
        
        return lines.get(index);
    }
    
    
    /**
     * プレイヤーごとにサイドバーを設定します
     * @param sclatPlayer
     * @param lines
     */
    public synchronized void setSidebarLine(SclatPlayer sclatPlayer, List<String> lines){
        List<String> oldLines = playerLineMap.get(sclatPlayer);
        if(oldLines != null) {
            Set<String> removeLines = playerRemoveLineMap.get(sclatPlayer);
            if (removeLines == null) {
                removeLines = new ConcurrentSet<>();
                playerRemoveLineMap.put(sclatPlayer, removeLines);
            }
        
            for (String oldLine : oldLines){
                if(!lines.contains(oldLine)) removeLines.add(oldLine);
            }
        }
        
        playerLineMap.put(sclatPlayer, lines);
        sclatPlayer.setScoreBoard(this);
    }
    
    
    /**
     * サイドバーをアップデートする
     */
    public void updateSidebar(Set<SclatPlayer> players){
        for(SclatPlayer sclatPlayer : players){
            Set<String> removeLines = playerRemoveLineMap.get(sclatPlayer);
            
            if(removeLines != null) {
                for (String line : removeLines) {
                    PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, "sclatSB", line, 0);
                    sclatPlayer.sendPacket(scorePacket);
                }
                removeLines.clear();
            }
    
            for(int index = 0; index < sidebarSize; index++) {
                PacketPlayOutScoreboardScore scorePacket = new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sclatSB", "sclatSB" + index, sidebarSize - index - 1);
                sclatPlayer.sendPacket(scorePacket);
            }
        }
    }
}
