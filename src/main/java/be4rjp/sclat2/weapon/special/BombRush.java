package be4rjp.sclat2.weapon.special;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.WeaponManager;
import be4rjp.sclat2.weapon.special.runnable.BombRushRunnable;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class BombRush extends SpecialWeapon {
    public BombRush(String id) {
        super(id);

        for(Lang lang : Lang.values()){
            this.displayName.put(lang, MessageManager.getText(lang, "sp-bomb-rush"));
        }
    }

    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        if(sclatPlayer.getSPWeaponProgress().getProgress() < 100 || !sclatPlayer.isCanUseSPWeapon()) return;
        sclatPlayer.setCanUseSPWeaponTime(6);
        sclatPlayer.setUsingBombRush(true);
        new BombRushRunnable(sclatPlayer).runTaskLaterAsynchronously(Sclat.getPlugin(), 120);
    }

    @Override
    public ItemStack getItemStack(Lang lang) {
        ItemStack itemStack = new ItemStack(Material.END_CRYSTAL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getDisplayName(lang));
        itemStack.setItemMeta(itemMeta);

        return WeaponManager.writeNBTTag(this, itemStack);
    }
}
