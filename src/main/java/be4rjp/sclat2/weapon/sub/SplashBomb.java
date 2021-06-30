package be4rjp.sclat2.weapon.sub;

public class SplashBomb extends SubWeapon{
    
    public static SplashBomb INSTANCE = new SplashBomb("splash_bomb");
    
    private SplashBomb(String id) {
        super(id);
        NEED_INK = 55.0F;
    }
}
