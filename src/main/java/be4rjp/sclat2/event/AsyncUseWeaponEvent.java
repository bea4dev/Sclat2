package be4rjp.sclat2.event;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

/**
 * プレイヤーが武器を持ってクリックした時に呼び出されるイベント
 */
public class AsyncUseWeaponEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final SclatPlayer sclatPlayer;
    private final SclatWeapon sclatWeapon;
    private final Action action;
    
    public AsyncUseWeaponEvent(SclatPlayer sclatPlayer, SclatWeapon sclatWeapon, Action action){
        super(true);
    
        this.sclatPlayer = sclatPlayer;
        this.sclatWeapon = sclatWeapon;
        this.action = action;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    
    public SclatPlayer getSclatPlayer() {return sclatPlayer;}
    
    public SclatWeapon getSclatWeapon() {return sclatWeapon;}
    
    public Action getAction() {return action;}
}
