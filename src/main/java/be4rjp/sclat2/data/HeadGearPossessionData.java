package be4rjp.sclat2.data;

import be4rjp.sclat2.player.costume.HeadGear;
import be4rjp.sclat2.player.costume.HeadGearData;
import be4rjp.sclat2.player.passive.Gear;

import java.util.Arrays;

public class HeadGearPossessionData extends SavableByteData{
    
    public HeadGearPossessionData() {
        super(2560);
    }
    
    /**
     * 所持しているヘッドギアのデータを返す
     * @param index インデックス (0 ~ 511)
     * @return
     */
    public HeadGearData getHeadGearData(int index){
        indexCheck(index);
        if (!hasHeadGear(index)) return null;
        
        byte[] data = Arrays.copyOfRange(bytes, index * 5, index * 5 + 5);
        
        int headGearNumber = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
        HeadGear headGear = HeadGear.getHeadGearBySaveNumber(headGearNumber);
        Gear gear1 = Gear.getGearByID(data[2]);
        Gear gear2 = Gear.getGearByID(data[3]);
        Gear gear3 = Gear.getGearByID(data[4]);
        
        return new HeadGearData(headGear, gear1, gear2, gear3);
    }
    
    
    /**
     * ヘッドギアのデータを追加する。最大で512個まで追加できる
     * @param headGearData
     * @return 追加に成功すれば true
     */
    public boolean addHeadGearData(HeadGearData headGearData){
        int index = this.getMaxIndexHasData();
        if(index == 511) return false;
        index += 1;
        
        bytes[index * 5] = (byte) (headGearData.headGear.getSaveNumber() >> 8);
        bytes[index * 5 + 1] = (byte) (headGearData.headGear.getSaveNumber() & 0xFF);
        bytes[index * 5 + 2] = (byte) headGearData.gear1.getSaveNumber();
        bytes[index * 5 + 3] = (byte) headGearData.gear2.getSaveNumber();
        bytes[index * 5 + 4] = (byte) headGearData.gear3.getSaveNumber();
        
        return true;
    }
    
    
    /**
     * 指定されたインデックスにヘッドギアのデータが存在するかどうかを返す
     * @param index インデックス (0 ~ 511)
     * @return 存在すれば true
     */
    public boolean hasHeadGear(int index){
        indexCheck(index);
        byte byte0 = bytes[index * 5];
        byte byte1 = bytes[index * 5 + 1];
        
        return byte0 != 0 || byte1 != 0;
    }
    
    
    /**
     * ヘッドギアのデータがある最終インデックスを返す
     * @return
     */
    public int getMaxIndexHasData(){
        for(int index = 0; index < 512; index++){
            if(!hasHeadGear(index)) return index - 1;
        }
        return 511;
    }
    
    
    private static void indexCheck(int index){
        if (index >= 512) throw new IllegalArgumentException("The index must be less than 512.");
    }
}
