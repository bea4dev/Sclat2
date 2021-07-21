package be4rjp.sclat2.weapon.main;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.main.runnable.ChargerRunnable;
import be4rjp.sclat2.weapon.main.ui.ChargerUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Charger extends MainWeapon {
    
    private int maxCharge = 10;
    private boolean scope = false;
    private double reach = 1.0;
    private ChargerUI chargerUI = new ChargerUI("null");
    
    public Charger(String id) {
        super(id);
    }
    
    @Override
    public void onRightClick(SclatPlayer sclatPlayer) {
        Player player = sclatPlayer.getBukkitPlayer();
        if(player != null){
            player.getInventory().setItem(10, new ItemStack(Material.ARROW));
        }
        
        ChargerRunnable runnable = (ChargerRunnable) sclatPlayer.getMainWeaponTaskMap().get(this);
        if(runnable == null){
            sclatPlayer.clearMainWeaponTasks();
            SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
            if(sclatTeam == null) return;
            runnable = new ChargerRunnable(this, sclatPlayer, sclatTeam);
            runnable.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
            sclatPlayer.getMainWeaponTaskMap().put(this, runnable);
        }
        runnable.setPlayerTick(0);
    }
    
    @Override
    public MainWeaponType getType() {
        return MainWeaponType.CHARGER;
    }
    
    @Override
    public void loadDetailsData() {
        if(yml.contains("max-charge")) this.maxCharge = yml.getInt("max-charge");
        if(yml.contains("scope")) this.scope = yml.getBoolean("scope");
        if(yml.contains("reach")) this.reach = yml.getDouble("reach");
        if(yml.contains("ui")) this.chargerUI = ChargerUI.getChargerUI(yml.getString("ui"));
    }
    
    public int getMaxCharge() {return maxCharge;}
    
    public boolean isScope() {return scope;}
    
    public double getReach() {return reach;}
    
    public ChargerUI getChargerUI() {return chargerUI;}
}
