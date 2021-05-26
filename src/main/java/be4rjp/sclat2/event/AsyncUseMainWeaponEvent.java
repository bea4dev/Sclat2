package be4rjp.sclat2.event;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

/**
 * プレイヤーがメインウエポンを持ってクリックした時に呼び出されるイベント
 */
public class AsyncUseMainWeaponEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final SclatPlayer sclatPlayer;
    private final MainWeapon mainWeapon;
    private final Action action;
    
    public AsyncUseMainWeaponEvent(SclatPlayer sclatPlayer, MainWeapon mainWeapon, Action action){
        super(true);
        
        this.sclatPlayer = sclatPlayer;
        this.mainWeapon = mainWeapon;
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
    
    public MainWeapon getMainWeapon() {return mainWeapon;}
    
    public Action getAction() {return action;}
}
