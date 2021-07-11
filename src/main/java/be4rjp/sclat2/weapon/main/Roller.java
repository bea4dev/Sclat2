package be4rjp.sclat2.weapon.main;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.main.runnable.RollerRunnable;
import be4rjp.sclat2.weapon.main.runnable.ShooterRunnable;

public class Roller extends MainWeapon {
    
    //撃ってから落ち始めるまでのtick
    private int fallTick = 0;
    //射撃間隔
    private int shootTick = 1;
    //射撃する弾の速度
    private double shootSpeed = 0.1;
    //射撃の角度間隔
    private double shootAccuracy = 5.0;
    //射撃の最大角度
    private double shootMaxAngle = 40.0;
    //二重に射撃される最大角度
    private double shootDoubleMaxAngle = 20.0;
    //ロールの横幅
    private double rollWidth = 3.0;
    //ロールのダメージ
    private float rollDamage = 25.0F;
    //ロールの必要インク
    private float rollNeedInk = 0.01F;
    //ロールのスピード
    private float rollWalkSpeed = 0.19F;
    
    
    
    public Roller(String id) {
        super(id);
    }
    
    public double getShootSpeed() {return shootSpeed;}
    
    public int getFallTick() {return fallTick;}
    
    public int getShootTick() {return shootTick;}
    
    public double getShootAccuracy() {return shootAccuracy;}
    
    public double getShootMaxAngle() {return shootMaxAngle;}
    
    public double getShootDoubleMaxAngle() {return shootDoubleMaxAngle;}
    
    public double getRollWidth() {return rollWidth;}
    
    public float getRollDamage() {return rollDamage;}
    
    public float getRollNeedInk() {return rollNeedInk;}
    
    public float getRollWalkSpeed() {return rollWalkSpeed;}
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        RollerRunnable runnable = (RollerRunnable) sclatPlayer.getMainWeaponTaskMap().get(this);
        if(runnable == null){
            sclatPlayer.clearMainWeaponTasks();
            SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
            if(sclatTeam == null) return;
            runnable = new RollerRunnable(this, sclatPlayer, sclatTeam);
            runnable.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
            sclatPlayer.getMainWeaponTaskMap().put(this, runnable);
        }
    
        runnable.setPlayerTick(0);
    }
    
    @Override
    public MainWeaponType getType() {
        return MainWeaponType.ROLLER;
    }
    
    @Override
    public void loadDetailsData() {
        if(yml.contains("fall-tick")) this.fallTick = yml.getInt("fall-tick");
        if(yml.contains("shoot-tick")) this.shootTick = yml.getInt("shoot-tick");
        if(yml.contains("shoot-speed")) this.shootSpeed = yml.getDouble("shoot-speed");
        if(yml.contains("shoot-accuracy")) this.shootAccuracy = yml.getDouble("shoot-accuracy");
        if(yml.contains("shoot-max-angle")) this.shootMaxAngle = yml.getDouble("shoot-max-angle");
        if(yml.contains("shoot-double-max-angle")) this.shootDoubleMaxAngle = yml.getDouble("shoot-double-max-angle");
        if(yml.contains("roll-width")) this.rollWidth = yml.getDouble("roll-width");
        if(yml.contains("roll-damage")) this.rollDamage = (float)yml.getDouble("roll-damage");
        if(yml.contains("roll-need-ink")) this.rollNeedInk = (float)yml.getDouble("roll-need-ink");
        if(yml.contains("roll-walk-speed")) this.rollWalkSpeed = (float)yml.getDouble("roll-walk-speed");
    }
}
