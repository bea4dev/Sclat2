package be4rjp.sclat2.entity.sub;

import be4rjp.blockstudio.BlockStudio;
import be4rjp.blockstudio.api.BSObject;
import be4rjp.blockstudio.api.BlockStudioAPI;
import be4rjp.blockstudio.file.ObjectData;
import be4rjp.sclat2.entity.SclatEntity;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SplashBombEntity implements SclatEntity {

    private final SclatPlayer sclatPlayer;
    private final SclatTeam sclatTeam;
    private final BSObject bsObject;

    private Location location;
    private Vector vector;

    public SplashBombEntity(SclatPlayer sclatPlayer, SclatTeam sclatTeam){
        this.sclatPlayer = sclatPlayer;
        this.sclatTeam = sclatTeam;
        this.location = sclatPlayer.getEyeLocation();
        this.vector = location.getDirection();

        BlockStudioAPI api = BlockStudio.getBlockStudioAPI();
        ObjectData objectData = api.getObjectData("splash_bomb");
        this.bsObject = api.createObjectFromObjectData("splash_bomb", location, objectData, false);
    }


    @Override
    public void tick() {

    }

    @Override
    public int getEntityID() {
        return 0;
    }

    @Override
    public void spawn() {
        bsObject.startTaskAsync(40);
    }

    @Override
    public void remove() {
        bsObject.remove();
    }
}
