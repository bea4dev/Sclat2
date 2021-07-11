package be4rjp.sclat2.data;

import be4rjp.sclat2.weapon.WeaponClass;

public class WeaponPossessionData extends SavableBitData{
    
    /**
     * 指定されたクラスを所持しているかどうかを返す
     * @param weaponClass
     * @return boolean
     */
    public boolean hasWeaponClass(WeaponClass weaponClass){return getBit(weaponClass.getSaveNumber());}
    
    /**
     * 指定されたクラスを所持させる
     * @param weaponClass
     */
    public void giveWeaponClass(WeaponClass weaponClass){setBit(weaponClass.getSaveNumber(), true);}
}
