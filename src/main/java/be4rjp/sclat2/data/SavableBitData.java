package be4rjp.sclat2.data;

import java.util.BitSet;

public abstract class SavableBitData implements SavableData{

    private BitSet bits = new BitSet();

    protected void setBit(int index, boolean bit){bits.set(index, bit);}

    protected boolean getBit(int index){return bits.get(index);}

    protected long[] getLongs(){return bits.toLongArray();}

    @Override
    public void save_to_sql(){
    
    }
    
    @Override
    public void load_from_sql(){
    
    }
}
