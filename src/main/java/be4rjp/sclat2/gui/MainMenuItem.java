package be4rjp.sclat2.gui;

import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MainMenuItem extends SclatWeapon {
    
    public static ItemStack getItemStack(Lang lang){
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(MessageManager.getText(lang, "gui-main-menu-item"));
        itemStack.setItemMeta(itemMeta);
    
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        Objects.requireNonNull(nmsItemStack.getTag()).setString("swid", "main-menu-nw");
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
    
    public MainMenuItem() {
        super("main-menu-nw");
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        MainMenuGUI.openMainMenu(sclatPlayer);
    }
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer) {
        MainMenuGUI.openMainMenu(sclatPlayer);
    }
}
