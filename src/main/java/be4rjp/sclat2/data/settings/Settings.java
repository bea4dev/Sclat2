package be4rjp.sclat2.data.settings;

public enum Settings {

    INK_ORBIT_PARTICLE(0x1, true),
    CHARGE_LASER_PARTICLE(0x2, true);

    private final int bitMask;
    private final boolean defaultSetting;

    Settings(int bitMask, boolean defaultSetting){
        this.bitMask = bitMask;
        this.defaultSetting = defaultSetting;
    }

    public int getBitMask() {return bitMask;}

    public boolean getDefaultSetting() {return defaultSetting;}
}
