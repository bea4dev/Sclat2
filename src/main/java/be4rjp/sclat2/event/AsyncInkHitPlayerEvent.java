package be4rjp.sclat2.event;

import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncInkHitPlayerEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final InkBullet inkBullet;
    private final SclatPlayer hitPlayer;
    
    public AsyncInkHitPlayerEvent(InkBullet inkBullet, SclatPlayer hitPlayer){
        super(true);
        this.inkBullet = inkBullet;
        this.hitPlayer = hitPlayer;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    public InkBullet getInkBullet() {return inkBullet;}
    
    public SclatPlayer getHitPlayer() {return hitPlayer;}
}
