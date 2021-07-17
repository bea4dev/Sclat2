package be4rjp.sclat2.data;

import java.util.Arrays;

public abstract class SavableByteData implements SavableData{
    
    protected byte[] bytes;
    
    protected final int size;
    
    protected SavableByteData(int size){
        this.bytes = new byte[size];
        this.size = size;
    }
    
    @Override
    public void load_from_byte_array(byte[] data) {
        this.bytes = new byte[size];
        for(int index = 0; index < data.length; index++){
            this.bytes[index] = data[index];
        }
    }
    
    @Override
    public byte[] write_to_byte_array() {
        return bytes;
    }
}
