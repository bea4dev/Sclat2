package be4rjp.sclat2.util;

import be4rjp.parallel.ParallelWorld;
import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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

    //一度にアップデートするブロックの最大数
    private static final int UPDATE_RATE = 50;

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
        //更新するブロックを収集する
        Set<Block> blocks = new HashSet<>();
        Map<Block, BlockData> writeMap = new HashMap<>();
        int index = 0;
        for(Map.Entry<Block, BlockData> entry : blockMap.entrySet()){
            writeMap.put(entry.getKey(), entry.getValue());
            blocks.add(entry.getKey());
            if(UPDATE_RATE == index) break;
            index++;
        }


        //Parallelを使ってプレイヤーごとにブロックを設置
        for(SclatPlayer sclatPlayer : match.getPlayers()){
            String uuid = sclatPlayer.getUUID();
            ParallelWorld parallelWorld = ParallelWorld.getParallelWorld(uuid);
            parallelWorld.setBlocks(writeMap, true);
        }

        //更新した分のデータを削除
        blocks.forEach(blockMap::remove);
    }


    /**
     * 非同期更新タスクをスタートさせる
     */
    public void start(){
        this.runTaskTimerAsynchronously(Sclat.getPlugin(), 0, 5);
    }
}
