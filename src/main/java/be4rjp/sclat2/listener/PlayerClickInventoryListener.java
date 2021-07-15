package be4rjp.sclat2.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class PlayerClickInventoryListener implements Listener {
    
    @EventHandler
    public void onClickInventory(InventoryClickEvent event){
        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }
    }
}
