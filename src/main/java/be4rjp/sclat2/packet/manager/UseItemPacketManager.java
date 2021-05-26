package be4rjp.sclat2.packet.manager;

import be4rjp.sclat2.packet.PacketHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.PacketPlayInUseItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class UseItemPacketManager extends BukkitRunnable {
    
    private final ChannelHandlerContext channelHandlerContext;
    private final Object packet;
    private final PacketHandler packetHandler;
    private final Player player;
    
    public UseItemPacketManager(ChannelHandlerContext channelHandlerContext, Object packet, PacketHandler packetHandler, Player player){
        this.channelHandlerContext = channelHandlerContext;
        this.packet = packet;
        this.packetHandler = packetHandler;
        this.player = player;
    }
    
    @Override
    public void run() {
        try{
            PacketPlayInUseItem useItem = (PacketPlayInUseItem) packet;
            ItemStack itemStack;
            if(useItem.b() == EnumHand.MAIN_HAND)
                itemStack = player.getInventory().getItemInMainHand();
            else
                itemStack = player.getInventory().getItemInOffHand();
            
            packetHandler.doRead(channelHandlerContext, packet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
