package be4rjp.sclat2.player;

import be4rjp.sclat2.player.passive.Passive;
import be4rjp.sclat2.util.SclatSound;
import be4rjp.sclat2.weapon.WeaponClass;
import org.bukkit.Sound;

public class SPWeaponProgress {

    private static final SclatSound FINISH_CHARGE_SOUND = new SclatSound(Sound.BLOCK_CHEST_OPEN, 0.8F, 2.0F);

    private final SclatPlayer sclatPlayer;
    private int maxNeedPoint = 250;
    private int point = 0;
    private int progress = 0;
    private boolean paintLock = false;


    public SPWeaponProgress(SclatPlayer sclatPlayer) {
        this.sclatPlayer = sclatPlayer;
    }

    public void initialize() {
        WeaponClass weaponClass = sclatPlayer.getWeaponClass();
        if (weaponClass == null) return;

        this.maxNeedPoint = (int) ((float) weaponClass.getSPWeaponNeedPoint() / sclatPlayer.getPassiveInfluence().getInfluence(Passive.SPECIAL_UP));
        this.progress = 0;
        this.point = 0;
        this.paintLock = false;
    }

    public synchronized void addPoint(int point) {
        if (paintLock) return;

        if (this.point + point >= maxNeedPoint) {
            if (this.point != maxNeedPoint) {
                sclatPlayer.playSound(FINISH_CHARGE_SOUND);
                sclatPlayer.sendText("sp-weapon-ready");
            }
            this.point = maxNeedPoint;
        } else {
            this.point += point;
        }

        this.progress = (int) (((float) this.point / (float) maxNeedPoint) * 100.0F);
    }

    public int getProgress() {return progress;}

    public void setProgress(int progress) {this.progress = progress;}

    public void setPoint(int point) {this.point = point;}

    public void setPaintLock(boolean paintLock) {this.paintLock = paintLock;}
}

