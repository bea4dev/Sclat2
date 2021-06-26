package be4rjp.sclat2.block;

import be4rjp.parallel.ParallelWorld;
import be4rjp.parallel.enums.UpdatePacketType;
import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.player.SclatPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutMultiBlockChange;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 決められた時間間隔でブロックを非同期でアップデート為のするクラス
 */
public class BlockUpdater extends BukkitRunnable {

    //このアップデーターを使用する試合のインスタンス
    private final Match match;
    //更新するブロックと適用するデータのマップ
    private final Map<Block, BlockData> blockMap = new ConcurrentHashMap<>();

    public BlockUpdater(Match match){
        this.match = match;
    }


    /**
     * ブロックを設置する
     * @param block 設置するブロック
     * @param blockData 設置するデータ
     */
    public void setBlock(Block block, BlockData blockData){
        blockMap.put(block, blockData);
    }

    /**
     * ブロックを設置する
     * @param block 設置するブロック
     * @param material 設置するデータ
     */
    public void setBlock(Block block, Material material){
        this.setBlock(block, material.createBlockData());
    }


    @Override
    public void run() {
        //Parallelを使ってプレイヤーごとにブロックを設置
        for(SclatPlayer sclatPlayer : match.getPlayers()){
            String uuid = sclatPlayer.getUUID();
            ParallelWorld parallelWorld = ParallelWorld.getParallelWorld(uuid);
            parallelWorld.setBlocks(blockMap, UpdatePacketType.MULTI_BLOCK_CHANGE);
        }
        
        blockMap.clear();
    }


    /**
     * 非同期更新タスクをスタートさせる
     */
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 5);
    }
}
