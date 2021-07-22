package be4rjp.sclat2.gui;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.gui.pagination.BackMenuPaginationButtonBuilder;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.MatchManager;
import be4rjp.sclat2.player.SclatPlayer;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchManagerGUI extends BukkitRunnable {
    
    private static Map<Lang, MatchManagerGUI> matchManagerGUIMap = new ConcurrentHashMap<>();
    
    public synchronized static MatchManagerGUI getMatchManagerGUI(Lang lang){
        return matchManagerGUIMap.computeIfAbsent(lang, k -> new MatchManagerGUI(lang).start());
    }
    
    
    
    private final SGMenu menu;
    private final Lang lang;
    
    private MatchManagerGUI(Lang lang){
        menu = Sclat.getSpiGUI().create(MessageManager.getText(lang, "gui-match-select"), 1);
        menu.setPaginationButtonBuilder(BackMenuPaginationButtonBuilder.getPaginationButtonBuilder(lang));
    
        this.lang = lang;
    }
    
    
    @Override
    public void run() {
        int index = 0;
        for(MatchManager matchManager : MatchManager.getMatchManagers()){
            Match match = matchManager.getMatch();
    
            List<String> lore = new ArrayList<>();
            Material material = Material.LIME_STAINED_GLASS;
            if(match != null){
                if(match.getMatchStatus() == Match.MatchStatus.WAITING) {
                    lore.add(String.format(MessageManager.getText(lang, "gui-match-waiting-player"), matchManager.getJoinedPlayers().size()));
                }
                
                if(match.getMatchStatus() == Match.MatchStatus.IN_PROGRESS){
                    lore.add(String.format(MessageManager.getText(lang, "gui-match-playing-player"), match.getPlayers().size()));
                }
            }
    
            if (matchManager.getJoinedPlayers().size() == 8) {
                material = Material.RED_STAINED_GLASS;
            }
            
            menu.setButton(index, new SGButton(new ItemBuilder(material).name(matchManager.getDisplayName())
                .lore(lore).build())
                .withListener(event -> {
                        if(!(event.getWhoClicked() instanceof Player)) return;
                        Player player = (Player) event.getWhoClicked();
                
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
    
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        matchManager.join(sclatPlayer);
                                        player.closeInventory();
                                    }
                                }.runTask(Sclat.getPlugin());
                            }
                        }.runTaskAsynchronously(Sclat.getPlugin());
                    }
                )
            );
            
            index++;
        }
    }
    
    public SGMenu getMenu() {return menu;}
    
    private MatchManagerGUI start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 20);
        return this;
    }
}
