package be4rjp.sclat2.entity;

/**
 * 非同期で処理する独自のエンティティ
 */
public interface SclatEntity {
    
    /**
     * 非同期で1tickごとに実行する処理
     */
    void tick();
    
    /**
     * エンティティのID
     * @return int EntityID
     */
    int getEntityID();
    
    /**
     * スポーンさせる
     */
    void spawn();
    
    /**
     * デスポーンさせる
     */
    void remove();
}
