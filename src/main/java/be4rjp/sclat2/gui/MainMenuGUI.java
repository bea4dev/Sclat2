package be4rjp.sclat2.gui;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.gui.pagination.BackMenuPaginationButtonBuilder;
import be4rjp.sclat2.gui.pagination.CloseMenuPaginationButtonBuilder;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class MainMenuGUI {
    public static void openMainMenu(SclatPlayer sclatPlayer){
        Player player = sclatPlayer.getBukkitPlayer();
        if(player == null) return;
    
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam != null){
            if(sclatTeam != Sclat.getLobbyTeam()){
                return;
            }
        }
    
        Lang lang = sclatPlayer.getLang();
        String menuName = MessageManager.getText(sclatPlayer.getLang(), "gui-main-menu");
    
        SGMenu menu = Sclat.getSpiGUI().create(menuName, 5);
        menu.setPaginationButtonBuilder(CloseMenuPaginationButtonBuilder.getPaginationButtonBuilder(lang));
    
       new BukkitRunnable() {
            @Override
            public void run() {
                menu.setButton(10, new SGButton(new ItemBuilder(Material.LIME_STAINED_GLASS)
                        .name(MessageManager.getText(lang, "gui-main-menu-join")).lore(MessageManager.getText(lang, "gui-main-menu-join-des")).build())
                        .withListener(event -> player.openInventory(MatchManagerGUI.getMatchManagerGUI(lang).getMenu().getInventory())));
    
                menu.setButton(12, new SGButton(new ItemBuilder(Material.WOODEN_HOE)
                        .name(MessageManager.getText(lang, "gui-main-menu-weapon")).lore(MessageManager.getText(lang, "gui-main-menu-weapon-des")).build())
                        .withListener(event -> WeaponClassGUI.openClassSelectGUI(sclatPlayer)));
    
                menu.setButton(14, new SGButton(new ItemBuilder(Material.WOODEN_HOE)
                        .name(MessageManager.getText(lang, "gui-main-menu-gear")).lore(MessageManager.getText(lang, "gui-main-menu-gear-des")).build())
                        .withListener(event -> HeadGearGUI.openHeadGearSelectGUI(sclatPlayer)));
    
                menu.setButton(16, new SGButton(new ItemBuilder(Material.PLAYER_HEAD).skullOwner(player.getName()).name(MessageManager.getText(lang, "gui-main-menu-status"))
                        .lore("", "&r&eCoin : " + sclatPlayer.getAchievementData().getCoin(), "&r&6Rank : " + sclatPlayer.getAchievementData().getRank(),
                                "&r&bKill : " + sclatPlayer.getAchievementData().getKill(), "&r&aPaint : " + sclatPlayer.getAchievementData().getPaint()).build()));
    
                Inventory inventory = menu.getInventory();
    
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.openInventory(inventory);
                    }
                }.runTask(Sclat.getPlugin());
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
    }
}
