package be4rjp.sclat2.data;

import be4rjp.sclat2.player.costume.HeadGear;
import be4rjp.sclat2.player.costume.HeadGearData;
import be4rjp.sclat2.player.passive.Gear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeadGearPossessionData extends SavableByteData{
    
    private final List<HeadGearData> headGearDataList = new ArrayList<>();
    
    public HeadGearPossessionData() {
        super(2560 + 5);
    }
    
    /**
     * 所持しているヘッドギアのデータを返す
     * @param index インデックス (0 ~ 511)
     * @return
     */
    public HeadGearData getHeadGearData(int index){
        indexCheck(index);
        if(headGearDataList.size() <= index) return null;
        return headGearDataList.get(index);
    }
    
    /**
     * SQLに書き込むためのbyte配列を作成する
     */
    public void writeToByteArray(){
        for(int index = 0; index < 512; index++){
            HeadGearData headGearData = this.getHeadGearData(index);
            if(headGearData == null) break;
    
            bytes[index * 5] = (byte) (headGearData.headGear.getSaveNumber() >> 8);
            bytes[index * 5 + 1] = (byte) (headGearData.headGear.getSaveNumber() & 0xFF);
            bytes[index * 5 + 2] = (byte) headGearData.gear1.getSaveNumber();
            bytes[index * 5 + 3] = (byte) headGearData.gear2.getSaveNumber();
            bytes[index * 5 + 4] = (byte) headGearData.gear3.getSaveNumber();
        }
    }
    
    /**
     * SQLからロードしたbyte配列から読み込む
     */
    public void loadFromByteArray(){
        this.headGearDataList.clear();
        
        for(int index = 0; index < 512; index++){
            byte[] data = Arrays.copyOfRange(bytes, index * 5, index * 5 + 5);
            
            if(data[0] == 0 && data[1] == 0) break;
    
            int headGearNumber = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
            HeadGear headGear = HeadGear.getHeadGearBySaveNumber(headGearNumber);
            Gear gear1 = Gear.getGearByID(data[2]);
            Gear gear2 = Gear.getGearByID(data[3]);
            Gear gear3 = Gear.getGearByID(data[4]);
    
            HeadGearData headGearData = new HeadGearData(headGear, gear1, gear2, gear3);
            this.headGearDataList.add(headGearData);
        }
    }
    
    /**
     * ヘッドギアのデータを追加する。最大で512個まで追加できる
     * @param headGearData
     * @return 追加に成功すれば true
     */
    public boolean addHeadGearData(HeadGearData headGearData){
        if(headGearDataList.size() >= 512) return false;
        
        headGearDataList.add(headGearData);
        return true;
    }
    
    /**
     * ヘッドギアのデータのリストをコピーして返す
     * @return List<HeadGearData>
     */
    public List<HeadGearData> getHeadGearDataList() {
        return new ArrayList<>(headGearDataList);
    }
    
    private static void indexCheck(int index){
        if (index >= 512) throw new IllegalArgumentException("The index must be less than 512.");
    }
    
    
    @Override
    public byte[] write_to_byte_array() {
        this.writeToByteArray();
        return super.write_to_byte_array();
    }
    
    
    @Override
    public void load_from_byte_array(byte[] data) {
        super.load_from_byte_array(data);
        this.loadFromByteArray();
    }
}
