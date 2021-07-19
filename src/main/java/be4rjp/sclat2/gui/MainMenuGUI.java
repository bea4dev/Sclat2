package be4rjp.sclat2.gui;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainMenuGUI {
    public static void openMainMenu(SclatPlayer sclatPlayer){
        Player player = sclatPlayer.getBukkitPlayer();
        if(player == null) return;
    
        Lang lang = sclatPlayer.getLang();
        String menuName = MessageManager.getText(sclatPlayer.getLang(), "gui-main-menu");
    
        SGMenu menu = Sclat.getSpiGUI().create(menuName, 5);
        menu.setPaginationButtonBuilder(LanguagePaginationButtonBuilder.getPaginationButtonBuilder(lang));
        
        menu.setButton(10, new SGButton(new ItemBuilder(Material.GREEN_STAINED_GLASS)
                .name(MessageManager.getText(lang, "gui-main-menu-join")).lore(MessageManager.getText(lang, "gui-main-menu-join-des")).build())
                .withListener(event -> sclatPlayer.sendMessage("join!")));
    
        menu.setButton(12, new SGButton(new ItemBuilder(Material.WOODEN_HOE)
                .name(MessageManager.getText(lang, "gui-main-menu-weapon")).lore(MessageManager.getText(lang, "gui-main-menu-weapon-des")).build())
                .withListener(event -> WeaponClassGUI.openClassSelectGUI(sclatPlayer)));
    
        menu.setButton(14, new SGButton(new ItemBuilder(Material.WOODEN_HOE)
                .name(MessageManager.getText(lang, "gui-main-menu-gear")).lore(MessageManager.getText(lang, "gui-main-menu-gear-des")).build())
                .withListener(event -> HeadGearGUI.openHeadGearSelectGUI(sclatPlayer)));
        
        
    }
}
