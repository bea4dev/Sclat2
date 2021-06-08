package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.PlayerLobbyMatch;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.RegionBlocks;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.WeaponManager;
import be4rjp.sclat2.weapon.main.Shooter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerJoinQuitListener implements Listener {
    
    private static int i = 0;
    private static PlayerLobbyMatch match;
    private static SclatTeam blue;
    private static SclatTeam orange;
    
    static {
        match = new PlayerLobbyMatch();
        blue = new SclatTeam(match, SclatColor.BLUE);
        orange = new SclatTeam(match, SclatColor.ORANGE);
        match.startBlockUpdate();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        sclatPlayer.updateBukkitPlayer();
        
        if(i % 2 == 0){
            orange.join(sclatPlayer);
        }else{
            blue.join(sclatPlayer);
        }
    
        MainWeapon wakaba = MainWeapon.getMainWeapon("wakaba");
        MainWeapon splat = MainWeapon.getMainWeapon("splat");
        MainWeapon gal52 = MainWeapon.getMainWeapon("52gal");
        player.getInventory().addItem(wakaba.getItemStack(Lang.en_US));
        player.getInventory().addItem(splat.getItemStack(Lang.ja_JP));
        player.getInventory().addItem(gal52.getItemStack(Lang.ja_JP));
        
        i++;
    }
    
    
    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!event.hasItem()) return;
        if(event.getHand() == null) return;
        if(event.getHand() != EquipmentSlot.HAND) return;
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        
        Location loc = player.getLocation();
        //InkBullet inkBullet = new InkBullet(sclatPlayer.getSclatTeam().getMatch(), player.getEyeLocation());
        //inkBullet.shootInitialize(sclatPlayer, player.getEyeLocation().getDirection(), 3);
        //inkBullet.spawn();
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
    }
}
