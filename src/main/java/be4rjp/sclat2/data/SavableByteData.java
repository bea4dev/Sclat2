package be4rjp.sclat2.data;

public abstract class SavableByteData implements SavableData{
    
    protected final byte[] bytes;
    
    protected SavableByteData(int size){
        this.bytes = new byte[size];
    }
    
    @Override
    public void load_from_sql() {
    
    }
    
    @Override
    public void save_to_sql() {
    
    }
}
