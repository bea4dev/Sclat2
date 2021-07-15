package be4rjp.sclat2.player.costume;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.passive.Gear;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HeadGearData {
    
    public final HeadGear headGear;
    
    //ギアパワー1
    public final Gear gear1;
    //ギアパワー2
    public final Gear gear2;
    //ギアパワー3
    public final Gear gear3;
    
    
    public HeadGearData(HeadGear headGear, Gear gear1, Gear gear2, Gear gear3){
        this.headGear = headGear;
        this.gear1 = gear1;
        this.gear2 = gear2;
        this.gear3 = gear3;
    }
    
    public ItemStack getItemStack(Lang lang){
        ItemStack itemStack = headGear.getItemStack(lang);
        ItemMeta itemMeta = itemStack.getItemMeta();
    
        List<String> lore = itemMeta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add("");
        lore.add(MessageManager.getText(lang, "head-gear-power"));
        lore.add(MessageManager.getText(lang, "head-gear-power1") + gear1.getDisplayName(lang));
        lore.add(MessageManager.getText(lang, "head-gear-power2") + gear2.getDisplayName(lang));
        lore.add(MessageManager.getText(lang, "head-gear-power3") + gear3.getDisplayName(lang));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public void setHeadGear(SclatPlayer sclatPlayer){
        List<Gear> gearList = sclatPlayer.getGearList();
        gearList.clear();
        gearList.add(gear1);
        gearList.add(gear2);
        gearList.add(gear3);
        
        Player player = sclatPlayer.getBukkitPlayer();
        if(player == null) return;
        
        player.getInventory().setHelmet(this.getItemStack(sclatPlayer.getLang()));
    }
}
