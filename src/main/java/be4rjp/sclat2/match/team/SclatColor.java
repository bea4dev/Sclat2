package be4rjp.sclat2.match.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SclatColor {
    
    BLUE("Blue", ChatColor.BLUE, Color.BLUE, Material.BLUE_WOOL, Material.BLUE_CONCRETE),
    ORANGE("Orange", ChatColor.GOLD, Color.ORANGE, Material.ORANGE_WOOL, Material.ORANGE_CONCRETE);
    
    private final String displayName;
    private final ChatColor chatColor;
    private final Color bukkitColor;
    private final Material wool;
    private final Material concrete;
    
    SclatColor(String displayName, ChatColor chatColor, Color bukkitColor, Material wool, Material concrete){
        this.displayName = chatColor + displayName + ChatColor.RESET;
        this.chatColor = chatColor;
        this.bukkitColor = bukkitColor;
        this.wool = wool;
        this.concrete = concrete;
    }
    
    
    public ChatColor getChatColor() {return chatColor;}
    
    public Color getBukkitColor() {return bukkitColor;}
    
    public Material getConcrete() {return concrete;}
    
    public Material getWool() {return wool;}
    
    public String getDisplayName() {return displayName;}
    
    
    /**
     * ランダムにSclatColorを取得します
     * @return SclatColor
     */
    public static SclatColor getRandomColor(){
        SclatColor[] sclatColors = SclatColor.values();
        int length = sclatColors.length;
        return sclatColors[new Random().nextInt(length)];
    }
    
    /**
     * ランダムにSclatColorのペアを取得します。
     * @return SclatColor[] (length = 2)
     */
    public static SclatColor[] getRandomColorPair(){
        SclatColor[] sclatColors = SclatColor.values();
        List<SclatColor> colorList = Arrays.asList(sclatColors);
        Collections.shuffle(colorList);
        return new SclatColor[]{colorList.get(0), colorList.get(1)};
    }
}
