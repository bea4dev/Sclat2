package be4rjp.sclat2.player.gear;

/**
 * ギア
 * 1.0 + (influence / (multi_influence_rate * 個数))
 */
public enum Gear {

    NO_GEAR(0.0F, 0.0F),
    IKA_SPEED_UP(0.2F, 0.2F);

    private final float influence;
    private final float multi_influence_rate;

    Gear(float default_influence, float multi_influence_rate){
        this.influence = default_influence * multi_influence_rate;
        this.multi_influence_rate = multi_influence_rate;
    }
}
