package be4rjp.sclat2.data;

import java.util.BitSet;

public abstract class SavableBitData implements SavableData{

    private BitSet bits = new BitSet();

    protected void setBit(int index, boolean bit){bits.set(index, bit);}

    protected boolean getBit(int index){return bits.get(index);}
    
    @Override
    public void load_from_byte_array(byte[] data) {
        bits = BitSet.valueOf(data);
    }
    
    @Override
    public byte[] write_to_byte_array() {
        return bits.toByteArray();
    }
}
