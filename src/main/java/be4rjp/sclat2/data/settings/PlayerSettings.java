package be4rjp.sclat2.data.settings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSettings {

    public final Map<Settings, Boolean> settingsBooleanMap;

    public PlayerSettings(){
        this.settingsBooleanMap = new ConcurrentHashMap<>();

        for(Settings settings : Settings.values()){
            settingsBooleanMap.put(settings, settings.getDefaultSetting());
        }
    }

    public boolean getSettings(Settings settings){return settingsBooleanMap.get(settings);}

    public int getCombinedID(){
        int data = 0;
        for(Settings settings : Settings.values()){
            boolean setting = settingsBooleanMap.get(settings);
            if(setting) data |= settings.getBitMask();
        }

        return data;
    }

    public void setByCombinedID(int data){
        settingsBooleanMap.keySet().forEach(settings -> settingsBooleanMap.put(settings, (data & settings.getBitMask()) != 0));
    }
}
