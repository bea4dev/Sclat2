package be4rjp.sclat2.event;

import be4rjp.sclat2.entity.InkBullet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncInkHitBlockEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final InkBullet inkBullet;
    private final Block hitBlock;
    
    public AsyncInkHitBlockEvent(InkBullet inkBullet, Block hitBlock){
        super(true);
        this.inkBullet = inkBullet;
        this.hitBlock = hitBlock;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    public InkBullet getInkBullet() {return inkBullet;}
    
    public Block getHitBlock() {return hitBlock;}
}
