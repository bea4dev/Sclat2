package be4rjp.sclat2.player.passive;

/**
 * ギア
 */
public enum Gear {

    NO_GEAR(Passive.NONE),
    IKA_SPEED_UP(Passive.IKA_SPEED_UP),
    HITO_SPEED_UP(Passive.HITO_SPEED_UP);

    private final Passive passive;

    Gear(Passive passive){
        this.passive = passive;
    }
    
    public Passive getPassive() {return passive;}
}
