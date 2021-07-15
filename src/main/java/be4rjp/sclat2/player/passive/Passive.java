package be4rjp.sclat2.player.passive;

/**
 * プレイヤーにつけるパッシブ効果
 * 1.0 + (influence / (multi_influence_rate * 個数))
 */
public enum Passive {
    
    //効果なし
    NONE(0.0F, 0.0F),
    //イカ状態のスピードアップ
    IKA_SPEED_UP(0.2F, 0.1F),
    //イカ状態のスピーダウン
    IKA_SPEED_DOWN(0.1F, 0.2F),
    //ヒト状態のスピードアップ
    HITO_SPEED_UP(0.2F, 0.1F),
    //ヒト状態のスピードダウン
    HITO_SPEED_DOWN(0.1F, 0.2F),
    //スペシャル増加量アップ
    SPECIAL_UP(0.1F, 0.2F);

    
    private final float influence;
    private final float multi_influence_rate;
    
    Passive(float default_influence, float multi_influence_rate){
        this.influence = default_influence * multi_influence_rate;
        this.multi_influence_rate = multi_influence_rate;
    }
    
    public float getInfluence() {return influence;}
    
    public float getMulti_influence_rate() {return multi_influence_rate;}
}
