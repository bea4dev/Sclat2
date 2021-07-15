package be4rjp.sclat2.gui;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.WeaponClass;
import be4rjp.sclat2.weapon.special.SpecialWeapon;
import be4rjp.sclat2.weapon.sub.SubWeapon;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WeaponClassGUI {
    
    public static void openClassSelectGUI(SclatPlayer sclatPlayer){
        Player player = sclatPlayer.getBukkitPlayer();
        if(player == null) return;
        
        Lang lang = sclatPlayer.getLang();
        String menuName = MessageManager.getText(sclatPlayer.getLang(), "gui-class-select");
    
        SGMenu menu = Sclat.getSpiGUI().create(menuName, 4);
        menu.setPaginationButtonBuilder(LanguagePaginationButtonBuilder.getPaginationButtonBuilder(lang));
        new BukkitRunnable() {
            @Override
            public void run() {
                for(int index = 0; index < 256; index++){
                    WeaponClass weaponClass = WeaponClass.getWeaponClassBySaveNumber(index);
                    if(weaponClass == null) break;
                    if(!sclatPlayer.getWeaponPossessionData().hasWeaponClass(weaponClass)) continue;
    
                    MainWeapon mainWeapon = weaponClass.getMainWeapon();
                    if(mainWeapon == null) continue;
                    
                    ItemStack itemStack = mainWeapon.getItemStack(lang);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    List<String> lines = itemMeta.getLore();
                    if(lines == null) lines = new ArrayList<>();
                    
                    SubWeapon subWeapon = weaponClass.getSubWeapon();
                    lines.add("");
                    if(subWeapon != null) lines.add(MessageManager.getText(lang, "gui-class-sub-weapon") + subWeapon.getDisplayName(lang));
                    lines.add("");
                    SpecialWeapon specialWeapon = weaponClass.getSpecialWeapon();
                    if(specialWeapon != null) lines.add(MessageManager.getText(lang, "gui-class-sp-weapon") + specialWeapon.getDisplayName(lang));
                    
                    itemMeta.setLore(lines);
                    itemStack.setItemMeta(itemMeta);
                    
                    menu.addButton(new SGButton(itemStack).withListener(event -> {
                        sclatPlayer.setWeaponClass(weaponClass);
                        sclatPlayer.createPassiveInfluence();
                        sclatPlayer.sendText("gui-class-selected", mainWeapon.getDisplayName(lang));
                        sclatPlayer.getSPWeaponProgress().initialize();
                        player.closeInventory();
                        HeadGearGUI.openHeadGearSelectGUI(sclatPlayer);
                    }));
                    Inventory menuInventory = menu.getInventory();
    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.openInventory(menuInventory);
                        }
                    }.runTask(Sclat.getPlugin());
                }
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
    
}
