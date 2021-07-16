package be4rjp.sclat2.gui;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.data.HeadGearPossessionData;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.costume.HeadGearData;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class HeadGearGUI {
    
    public static void openHeadGearSelectGUI(SclatPlayer sclatPlayer) {
        Player player = sclatPlayer.getBukkitPlayer();
        if (player == null) return;
    
        Lang lang = sclatPlayer.getLang();
        String menuName = MessageManager.getText(sclatPlayer.getLang(), "gui-head-gear-select");
    
        SGMenu menu = Sclat.getSpiGUI().create(menuName, 4);
        menu.setPaginationButtonBuilder(LanguagePaginationButtonBuilder.getPaginationButtonBuilder(lang));
        new BukkitRunnable() {
            @Override
            public void run() {
                HeadGearPossessionData possessionData = sclatPlayer.getHeadGearPossessionData();
                for (int index = 0; index < 512; index++) {
                    HeadGearData headGearData = possessionData.getHeadGearData(index);
                    if(headGearData == null) break;
                    
                    menu.addButton(new SGButton(headGearData.getItemStack(sclatPlayer.getLang())).withListener(event -> {
                        sclatPlayer.setHeadGearData(headGearData);
                        sclatPlayer.createPassiveInfluence();
                        sclatPlayer.sendText("gui-head-gear-selected", headGearData.headGear.getDisplayName(lang));
                        player.closeInventory();
                    }));
                }
                
                Inventory menuInventory = menu.getInventory();
    
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.openInventory(menuInventory);
                    }
                }.runTask(Sclat.getPlugin());
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
    
}
