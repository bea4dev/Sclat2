package be4rjp.sclat2.packet;

import be4rjp.sclat2.Sclat;
import io.netty.channel.*;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.nio.channels.ClosedChannelException;

public class PacketHandler extends ChannelDuplexHandler {
    
    private final Player player;
    private final EntityPlayer entityPlayer;
    
    public PacketHandler(Player player){
        this.player = player;
        this.entityPlayer = ((CraftPlayer)player).getHandle();
    }
    
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
    
        super.channelRead(channelHandlerContext, packet);
    }
    
    
    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
    
        super.write(channelHandlerContext, packet, channelPromise);
    }
    
    
    
    public void doRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception{
        try {
            Channel channel = entityPlayer.playerConnection.networkManager.channel;
            
            ChannelHandler channelHandler = channel.pipeline().get(Sclat.getPlugin().getName() + "PacketInjector:" + player.getName());
            if(channelHandler != null && player.isOnline()) {
                super.channelRead(channelHandlerContext, packet);
            }
        }catch (ClosedChannelException e){/**/}
    }
    
    public void doWrite(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception{
        try {
            Channel channel = entityPlayer.playerConnection.networkManager.channel;
            
            ChannelHandler channelHandler = channel.pipeline().get(Sclat.getPlugin().getName() + "PacketInjector:" + player.getName());
            if(channelHandler != null && player.isOnline()) {
                super.write(channelHandlerContext, packet, channelPromise);
            }
        }catch (ClosedChannelException e){/**/}
    }
}
