package be4rjp.sclat2.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SclatSound {
    
    private final Sound sound;
    private final float volume;
    private final float pitch;
    
    public SclatSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public void play(Player player, Location location){
        player.playSound(location, sound, volume, pitch);
    }
    
    public Sound getSound() {return sound;}
    
    public float getPitch() {return pitch;}
    
    public float getVolume() {return volume;}
}
