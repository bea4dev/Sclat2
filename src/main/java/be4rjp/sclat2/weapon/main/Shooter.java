package be4rjp.sclat2.weapon.main;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.main.runnable.ShooterRunnable;

public class Shooter extends MainWeapon {
    
    //撃ってから落ち始めるまでのtick
    private int fallTick = 0;
    //射撃間隔
    private int shootTick = 1;
    //射撃する弾の速度
    private double shootSpeed = 0.1;
    //リコイル
    private ShooterRecoil recoil = new ShooterRecoil();
    
    
    public Shooter(String id) {
        super(id);
    }
    
    public double getShootSpeed() {return shootSpeed;}
    
    public int getFallTick() {return fallTick;}
    
    public int getShootTick() {return shootTick;}
    
    public ShooterRecoil getRecoil() {return recoil;}
    
    @Override
    public void onLeftClick(SclatPlayer sclatPlayer) {
    
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        ShooterRunnable runnable = (ShooterRunnable) sclatPlayer.getMainWeaponTaskMap().get(this);
        if(runnable == null) return;
        
        runnable.setPlayerTick(0);
    }
    
    @Override
    public MainWeaponType getType() {
        return MainWeaponType.SHOOTER;
    }
    
    @Override
    public void loadDetailsData() {
        if(yml.contains("fall-tick")) this.fallTick = yml.getInt("fall-tick");
        if(yml.contains("shoot-tick")) this.shootTick = yml.getInt("shoot-tick");
        if(yml.contains("shoot-speed")) this.shootSpeed = yml.getDouble("shoot-speed");
        
        if(yml.contains("recoil")){
            if(yml.contains("recoil.shoot-random")) recoil.setShootRandom(yml.getDouble("recoil.shoot-random"));
            if(yml.contains("recoil.shoot-max-random")) recoil.setShootMaxRandom(yml.getDouble("recoil.shoot-max-random"));
            if(yml.contains("recoil.increase-min-tick")) recoil.setMinTick(yml.getInt("recoil.increase-min-tick"));
            if(yml.contains("recoil.increase-max-tick")) recoil.setMaxTick(yml.getInt("recoil.increase-max-tick"));
            if(yml.contains("recoil.increase-reset-tick")) recoil.setResetTick(yml.getInt("recoil.increase-reset-tick"));
        }
    }
    
    
    public class ShooterRecoil{
        //撃った時の弾の散らばり
        private double shootRandom = 0.0;
        //撃った時の弾の散らばりの最大値
        private double shootMaxRandom = 1.0;
        //散らばりが増加し始めるtick
        private int minTick = 10;
        //散らばりが最大値に達するtick
        private int maxTick = 30;
        //撃つのをやめてから散らばりがリセットされるtick
        private int resetTick = 40;
    
        /**
         * 散らばりが最大値に達するtick
         * @param maxTick
         */
        public void setMaxTick(int maxTick) {this.maxTick = maxTick;}
    
        /**
         * 散らばりが増加し始めるtick
         * @param minTick
         */
        public void setMinTick(int minTick) {this.minTick = minTick;}
    
        /**
         * 撃つのをやめてから散らばりがリセットされるtick
         * @param resetTick
         */
        public void setResetTick(int resetTick) {this.resetTick = resetTick;}
    
        /**
         * 撃った時の弾の散らばりの最大値
         * @param shootMaxRandom
         */
        public void setShootMaxRandom(double shootMaxRandom) {this.shootMaxRandom = shootMaxRandom;}
    
        /**
         * 撃った時の弾の散らばり
         * @param shootRandom
         */
        public void setShootRandom(double shootRandom) {this.shootRandom = shootRandom;}
    
        /**
         * 撃つのをやめてから散らばりがリセットされるtick
         * @return int
         */
        public int getResetTick() {return resetTick;}
    
        public double getShootRandomRange(int clickTick){
            if(clickTick <= minTick){
                return shootRandom;
            }else{
                if(clickTick < maxTick){
                    double rate = (double)(clickTick - minTick) / (double)(maxTick - minTick);
                    double m = shootMaxRandom - shootRandom;
                    return shootRandom + (m * rate);
                }else{
                    return shootMaxRandom;
                }
            }
        }
    }
}
