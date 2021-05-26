package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.InkBullet;
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
    
        PlayerLobbyMatch match = new PlayerLobbyMatch();
        SclatTeam sclatTeam = new SclatTeam(match, SclatColor.BLUE);
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        match.startBlockUpdate();
        sclatTeam.join(sclatPlayer);
    
        MainWeapon wakaba = MainWeapon.getMainWeapon("wakaba");
        wakaba.getType().createMainWeaponRunnableInstance(wakaba, sclatPlayer).runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);
        player.getInventory().addItem(wakaba.getItemStack());
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
