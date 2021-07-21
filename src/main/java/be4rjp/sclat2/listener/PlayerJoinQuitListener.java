package be4rjp.sclat2.listener;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.SclatConfig;
import be4rjp.sclat2.gui.MainMenuItem;
import be4rjp.sclat2.packet.PacketHandler;
import be4rjp.sclat2.player.SclatPlayer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.papermc.lib.PaperLib;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerJoinQuitListener implements Listener {
    
    private static int i = 0;
    
    
    static {
    
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PaperLib.teleportAsync(player, SclatConfig.getJoinLocation());
    
    
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setWalkSpeed(0.2F);
                player.getInventory().clear();
    
                SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                sclatPlayer.updateBukkitPlayer();
                sclatPlayer.sendSkinRequest();
    
                try {
                    sclatPlayer.loadAchievementFromSQL();
                }catch (Exception e){
                    player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(0, Note.Tone.G));
                    Date dateObj = new Date();
                    SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
                    player.sendMessage("§c§n以下の理由により正常にセーブデータを読み込むことができませんでした。");
                    player.sendMessage("§c§n再度接続し直しても同じエラーが出る場合は運営に報告してください。");
                    player.sendMessage("§c§nThe save data could not be loaded properly for the following reasons.");
                    player.sendMessage("§c§nIf you still get the same error after trying to connect again, please report it to the administrators.");
                    player.sendMessage("");
                    player.sendMessage("§eError (" + format.format(dateObj) + ") : ");
                    player.sendMessage(e.getMessage());
                    e.printStackTrace();
                    
                    return;
                }
                
                sclatPlayer.teleport(SclatConfig.getLobbyLocation());
                player.getInventory().setItem(6, MainMenuItem.getItemStack(sclatPlayer.getLang()));
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
        
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
    
        new BukkitRunnable() {
            @Override
            public void run() {
                SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                try {
                    sclatPlayer.saveAchievementToSQL();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Sclat.getPlugin());
        
        try {
            Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
            
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(Sclat.getPlugin().getName() + "PacketInjector:" + player.getName());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        PaperLib.teleportAsync(player, SclatConfig.getJoinLocation());
    }
}
