package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.entity.InkBullet;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.NawabariMatch;
import be4rjp.sclat2.match.PlayerLobbyMatch;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchWaitRunnable;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.packet.PacketHandler;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.util.RegionBlocks;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.WeaponManager;
import be4rjp.sclat2.weapon.main.Shooter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
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
    private static Match match;
    private static SclatTeam blue;
    private static SclatTeam orange;
    private static MatchWaitRunnable waitRunnable;
    
    static {
        match = new NawabariMatch(SclatMap.getSclatMap("shionome"));
        blue = new SclatTeam(match, SclatColor.BLUE);
        orange = new SclatTeam(match, SclatColor.ORANGE);
        match.initialize();
        waitRunnable = new MatchWaitRunnable(match);
        waitRunnable.start();
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
        sclatPlayer.setLang(Lang.en_US);
    
        MainWeapon wakaba = MainWeapon.getMainWeapon("wakaba");
        MainWeapon splat = MainWeapon.getMainWeapon("splat");
        MainWeapon gal52 = MainWeapon.getMainWeapon("52gal");
        Lang lang = sclatPlayer.getLang();
        player.getInventory().clear();
        player.getInventory().addItem(wakaba.getItemStack(lang));
        player.getInventory().addItem(splat.getItemStack(lang));
        player.getInventory().addItem(gal52.getItemStack(lang));
        
        i++;
    }
    
    
    @EventHandler
    public void onjoin(PlayerJoinEvent event){
        //Inject packet handler
        Player player = event.getPlayer();
        
        PacketHandler packetHandler = new PacketHandler(player);
        
        try {
            ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
            pipeline.addBefore("packet_handler", Sclat.getPlugin().getName() + "PacketInjector:" + player.getName(), packetHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @EventHandler
    public void onleave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        
        try {
            Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
            
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(Sclat.getPlugin().getName() + "PacketInjector:" + player.getName());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
