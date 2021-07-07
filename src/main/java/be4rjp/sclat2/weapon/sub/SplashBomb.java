package be4rjp.sclat2.weapon.sub;

import be4rjp.sclat2.entity.sub.SplashBombEntity;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class SplashBomb extends SubWeapon{
    
    public SplashBomb(String id) {
        super(id);
        this.needInk = 60.0F;
        this.damage = 30.0F;
        
        for(Lang lang : Lang.values()){
            this.displayName.put(lang, MessageManager.getText(lang, "sub-splash-bomb"));
        }
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam == null) return;
        SplashBombEntity bombEntity = new SplashBombEntity(sclatPlayer.getEyeLocation(), sclatPlayer.getEyeLocation().getDirection(), sclatTeam, sclatPlayer);
        bombEntity.spawn();
    }
    
    @Override
    public ItemStack getItemStack(SclatTeam sclatTeam, Lang lang) {
        ItemStack itemStack = new ItemStack(sclatTeam.getSclatColor().getWool());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getDisplayName(lang));
        itemStack.setItemMeta(itemMeta);
    
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        Objects.requireNonNull(nmsItemStack.getTag()).setString("swid", id);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
}
