package be4rjp.sclat2.data;

/**
 * SQLに保存可能なデータ
 */
public interface SavableData {
    
    void save_to_sql();
    
    void load_from_sql();
}
