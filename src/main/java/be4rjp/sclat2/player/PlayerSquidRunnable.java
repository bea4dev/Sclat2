package be4rjp.sclat2.player;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.block.PaintData;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.util.SclatSound;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class PlayerSquidRunnable extends BukkitRunnable {
    
    public static final MobEffect ON_INK_EFFECT = new MobEffect(MobEffectList.fromId(PotionEffectType.REGENERATION.getId()), Integer.MAX_VALUE, 0, false, false, true, null);
    public static final MobEffect ON_INK_EFFECT_REMOVE = new MobEffect(MobEffectList.fromId(PotionEffectType.REGENERATION.getId()), 0, 0, false, false, true, ON_INK_EFFECT);
    public static final MobEffect ON_ENEMY_INK_EFFECT = new MobEffect(MobEffectList.fromId(PotionEffectType.POISON.getId()), Integer.MAX_VALUE, 0, false, false, true, null);
    public static final MobEffect ON_ENEMY_INK_EFFECT_REMOVE = new MobEffect(MobEffectList.fromId(PotionEffectType.POISON.getId()), 0, 0, false, false, true, ON_ENEMY_INK_EFFECT);
    
    
    public static final float ON_INK_RECOVERY = 0.027F;
    public static final float NORMAL_RECOVERY = 0.0006F;
    
    public static final float ON_INK_SPEED = 0.25F;
    public static final float NORMAL_SPEED = 0.13F;
    
    private static final SclatSound IN_INK_SOUND = new SclatSound(Sound.ITEM_BUCKET_FILL, 0.5F, 1F);
    private static final SclatSound OUT_INK_SOUND = new SclatSound(Sound.ENTITY_PLAYER_SWIM, 0.3F, 5F);
    
    private final SclatPlayer sclatPlayer;
    private final EntitySquid squid;
    
    private boolean isOnEnemyInk = false;
    private PacketSendFlag packetFlag = PacketSendFlag.SENT_DESTROY_PACKET;
    
    public PlayerSquidRunnable(SclatPlayer sclatPlayer){
        this.sclatPlayer = sclatPlayer;
        
        this.squid = new EntitySquid(EntityTypes.SQUID, ((CraftWorld)sclatPlayer.getLocation().getWorld()).getHandle());
        Location loc = sclatPlayer.getLocation();
        squid.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        squid.setCustomName(CraftChatMessage.fromStringOrNull(sclatPlayer.getDisplayName()));
        squid.setCustomNameVisible(true);
        if(sclatPlayer.getSclatTeam() != null){
            sclatPlayer.getSclatTeam().getScoreBoardTeam().addEntry(squid.getBukkitEntity().getUniqueId().toString());
        }
        
        sclatPlayer.setFOV(0.1F);
    }
    
    @Override
    public void run() {
        if(!sclatPlayer.isOnline()){
            this.cancel();
        }
        
        SclatTeam sclatTeam = sclatPlayer.getSclatTeam();
        if(sclatTeam == null) return;
        Match match = sclatTeam.getMatch();
        
        //足元のインクのチェック
        boolean onInk = false;
        boolean onEnemyInk = false;
        PaintData pd1 = match.getPaintData(sclatPlayer.getLocation().add(0.0, -0.5, 0.0).getBlock());
        if(pd1 != null){
            if(pd1.getSclatTeam() == sclatTeam){
                onInk = true;
            }else{
                onEnemyInk = true;
            }
        }
        
        //敵インクの上にいるときの処理
        if(onEnemyInk){
            if(!isOnEnemyInk){
                PacketPlayOutEntityEffect entityEffect = new PacketPlayOutEntityEffect(sclatPlayer.getEntityID(), ON_ENEMY_INK_EFFECT);
                sclatPlayer.sendPacket(entityEffect);
                isOnEnemyInk = true;
            }
        }else{
            if(isOnEnemyInk){
                PacketPlayOutEntityEffect entityEffect = new PacketPlayOutEntityEffect(sclatPlayer.getEntityID(), ON_ENEMY_INK_EFFECT_REMOVE);
                sclatPlayer.sendPacket(entityEffect);
                isOnEnemyInk = false;
            }
        }
        
        //壁のブロックが塗られているかどうかのチェック
        boolean onWallInk = false;
        Set<Block> blocks = new HashSet<>();
        Location head = sclatPlayer.getLocation().add(0.0, 0.5, 0.0);
        blocks.add(head.clone().add(0.5, 0.0, 0.5).getBlock());
        blocks.add(head.clone().add(-0.5, 0.0, 0.5).getBlock());
        blocks.add(head.clone().add(0.5, 0.0, -0.5).getBlock());
        blocks.add(head.clone().add(-0.5, 0.0, -0.5).getBlock());
        for(Block block : blocks){
            PaintData paintData = match.getPaintData(block);
            if(paintData == null) continue;
            if(paintData.getSclatTeam() == sclatTeam){
                onWallInk = true;
                break;
            }
        }
    
        boolean isSquid = false;
        Player player = sclatPlayer.getBukkitPlayer();
        if(player != null) {
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) isSquid = true;
            
            //イカ座標設定
            Location loc = sclatPlayer.getLocation();
            squid.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            
            //イカ表示切り替え
            boolean showSquid = false;
            if (isSquid){
                if(!(onInk || onWallInk)){
                    showSquid = true;
                }
            }
            if(showSquid){
                if(packetFlag == PacketSendFlag.SENT_DESTROY_PACKET){
                    PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(squid);
                    PacketPlayOutEntityMetadata squidMetadata = new PacketPlayOutEntityMetadata(squid.getId(), squid.getDataWatcher(), true);
                    match.getPlayers().forEach(op -> {
                        op.sendPacket(spawn);
                        op.sendPacket(squidMetadata);
                    });
                    packetFlag = PacketSendFlag.SENT_SPAWN_PACKET;
                }
                PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(squid);
                match.getPlayers().forEach(op -> op.sendPacket(teleport));
            }else{
                if (packetFlag == PacketSendFlag.SENT_SPAWN_PACKET) {
                    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(squid.getId());
                    match.getPlayers().forEach(op -> op.sendPacket(destroy));
                    packetFlag = PacketSendFlag.SENT_DESTROY_PACKET;
                }
            }
        }
        
    
        //サウンド再生とプレイヤーの移動速度とプレイヤーの表示
        if(isSquid && (onInk || onWallInk)){
            if(!(sclatPlayer.isSquid() && sclatPlayer.isOnInk())){
                sclatPlayer.playSound(IN_INK_SOUND);
                sclatPlayer.setWalkSpeed(ON_INK_SPEED);
                PacketPlayOutEntityEffect entityEffect = new PacketPlayOutEntityEffect(sclatPlayer.getEntityID(), ON_INK_EFFECT);
                sclatPlayer.sendPacket(entityEffect);
    
                //プレイヤー表示
                EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
                entityPlayer.setInvisible(true);
                PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
                match.getPlayers().forEach(op -> op.sendPacket(metadata));
            }
        }else{
            if(sclatPlayer.isSquid() && sclatPlayer.isOnInk()){
                sclatPlayer.playSound(OUT_INK_SOUND);
                sclatPlayer.setWalkSpeed(NORMAL_SPEED);
                PacketPlayOutEntityEffect entityEffect = new PacketPlayOutEntityEffect(sclatPlayer.getEntityID(), ON_INK_EFFECT_REMOVE);
                sclatPlayer.sendPacket(entityEffect);
                
                if(player != null){
                    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
                    entityPlayer.setInvisible(false);
                    PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
                    match.getPlayers().forEach(op -> op.sendPacket(metadata));
                }
            }
        }
    
        //SclatPlayerへ情報をセット
        sclatPlayer.setOnInk(onInk || onWallInk);
        if(onWallInk){
            if(!sclatPlayer.isFly()){
                sclatPlayer.setFly(true);
            }
        }else{
            if(sclatPlayer.isFly()){
                sclatPlayer.setFly(false);
            }
        }
        sclatPlayer.setSquid(isSquid);
    
        
        //インク回復
        if(sclatPlayer.isOnInk() && sclatPlayer.isSquid()){
            sclatPlayer.addInk(ON_INK_RECOVERY);
        }else{
            sclatPlayer.addInk(NORMAL_RECOVERY);
        }
    }
    
    
    public void start(){this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 1);}
    
    
    public enum PacketSendFlag{
        SENT_SPAWN_PACKET,
        SENT_DESTROY_PACKET
    }
}
