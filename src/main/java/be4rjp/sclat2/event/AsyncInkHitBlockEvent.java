package be4rjp.sclat2.event;

import be4rjp.sclat2.entity.ink.InkBullet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncInkHitBlockEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final InkBullet inkBullet;
    private final Block hitBlock;
    private final Location hitLocation;
    
    public AsyncInkHitBlockEvent(InkBullet inkBullet, Block hitBlock, Location hitLocation){
        super(true);
        this.inkBullet = inkBullet;
        this.hitBlock = hitBlock;
        this.hitLocation = hitLocation;
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
    
    public Location getHitLocation() {return hitLocation;}
}
