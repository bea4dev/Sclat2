package be4rjp.sclat2.data;

import be4rjp.sclat2.player.SclatPlayer;

public class AchievementData {
    
    private final SclatPlayer sclatPlayer;
    
    private int paint = 0;
    private int kill = 0;
    private int rank = 0;
    
    private int coin = 0;
    
    private final WeaponPossessionData weaponPossessionData;
    private final HeadGearPossessionData headGearPossessionData;
    
    public AchievementData(SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;
        
        this.weaponPossessionData = sclatPlayer.getWeaponPossessionData();
        this.headGearPossessionData = sclatPlayer.getHeadGearPossessionData();
    }
    
    public int getKill() {return kill;}
    
    public int getPaint() {return paint;}
    
    public int getRank() {return rank;}
    
    public synchronized int getCoin() {return coin;}
    
    public void addKill(int kill){this.kill += kill;}
    
    public void addPaint(int paint){this.paint += paint;}
    
    public void addRank(int rank){this.rank += rank;}
    
    public synchronized void addCoin(int coin){this.coin += coin;}
    
    public void setKill(int kill) {this.kill = kill;}
    
    public void setPaint(int paint) {this.paint = paint;}
    
    public void setRank(int rank) {this.rank = rank;}
    
    public synchronized void setCoin(int coin) {this.coin = coin;}
    
    public HeadGearPossessionData getHeadGearPossessionData() {return headGearPossessionData;}
    
    public WeaponPossessionData getWeaponPossessionData() {return weaponPossessionData;}
    
    public SclatPlayer getSclatPlayer() {return sclatPlayer;}
}
