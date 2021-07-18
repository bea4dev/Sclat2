package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.gui.WeaponClassGUI;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.MatchManager;
import be4rjp.sclat2.match.NawabariMatch;
import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.runnable.MatchWaitRunnable;
import be4rjp.sclat2.match.team.SclatColor;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.packet.PacketHandler;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.player.costume.HeadGear;
import be4rjp.sclat2.player.costume.HeadGearData;
import be4rjp.sclat2.player.passive.Gear;
import be4rjp.sclat2.weapon.MainWeapon;
import be4rjp.sclat2.weapon.SclatWeapon;
import be4rjp.sclat2.weapon.WeaponClass;
import be4rjp.sclat2.weapon.sub.SubWeapon;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
    
    private static int i = 0;
    
    
    static {
    
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.getInventory().clear();
        
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        sclatPlayer.updateBukkitPlayer();
        sclatPlayer.sendSkinRequest();
        
        try {
            sclatPlayer.loadAchievementFromSQL();
        }catch (Exception e){
            player.sendMessage("§cFailed to connect to MySQL database!");
            player.sendMessage(e.getMessage());
            e.printStackTrace();
        }
        
        sclatPlayer.setLang(Lang.en_US);
        
        
        //sclatPlayer.setLang(i % 2 == 0 ? Lang.ja_JP : Lang.en_US);

        MatchManager.getMatchManager("azi").join(sclatPlayer);
        
        Lang lang = sclatPlayer.getLang();
        
    
        for(int index = 0; index < 256; index++) {
            WeaponClass classBySaveNumber = WeaponClass.getWeaponClassBySaveNumber(index);
            if (classBySaveNumber == null) break;
            sclatPlayer.getWeaponPossessionData().giveWeaponClass(classBySaveNumber);
        }
        
        sclatPlayer.getHeadGearPossessionData().addHeadGearData(new HeadGearData(HeadGear.getHeadGearBySaveNumber(1), Gear.IKA_SPEED_UP, Gear.IKA_SPEED_UP, Gear.IKA_SPEED_UP));
        sclatPlayer.getHeadGearPossessionData().addHeadGearData(new HeadGearData(HeadGear.getHeadGearBySaveNumber(1), Gear.HITO_SPEED_UP, Gear.NO_GEAR, Gear.IKA_SPEED_UP));
        sclatPlayer.getHeadGearPossessionData().addHeadGearData(new HeadGearData(HeadGear.getHeadGearBySaveNumber(1), Gear.NO_GEAR, Gear.NO_GEAR, Gear.NO_GEAR));
        sclatPlayer.getHeadGearPossessionData().addHeadGearData(new HeadGearData(HeadGear.getHeadGearBySaveNumber(1), Gear.HITO_SPEED_UP, Gear.HITO_SPEED_UP, Gear.HITO_SPEED_UP));
        
        WeaponClassGUI.openClassSelectGUI(sclatPlayer);
        
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
    
        SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
        try {
            sclatPlayer.saveAchievementToSQL();
        }catch (Exception e){
            Sclat.getPlugin().getLogger().info("§cFailed to connect to MySQL database!");
            Sclat.getPlugin().getLogger().info(e.getMessage());
            e.printStackTrace();
        }
        
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
