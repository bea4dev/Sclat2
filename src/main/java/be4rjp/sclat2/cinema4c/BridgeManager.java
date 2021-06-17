package be4rjp.sclat2.cinema4c;

import be4rjp.cinema4c.bridge.Cinema4CBridge;

public class BridgeManager {
    public static void registerPluginBridge(){
        Cinema4CBridge.registerPluginBridge("Sclat", new C4CBridge());
    }
}
