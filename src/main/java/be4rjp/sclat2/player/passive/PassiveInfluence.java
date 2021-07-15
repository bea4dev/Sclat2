package be4rjp.sclat2.player.passive;

import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.WeaponClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PassiveInfluence {
    
    private final Map<Passive, Float> passiveInfluenceMap;
    
    public PassiveInfluence(){
        passiveInfluenceMap = new ConcurrentHashMap<>();
        
        for(Passive passive : Passive.values()){
            passiveInfluenceMap.put(passive, 0.0F);
        }
    }
    
    
    public void createPassiveInfluence(SclatPlayer sclatPlayer){
        List<Passive> passiveList = new ArrayList<>();
        
        //プレイヤーについているギアのパッシブ効果を取得
        sclatPlayer.getGearList().forEach(gear -> passiveList.add(gear.getPassive()));
    
        //メイン武器のパッシブ効果を取得
        WeaponClass weaponClass = sclatPlayer.getWeaponClass();
        if(weaponClass != null){
            if(weaponClass.getMainWeapon() != null){
                passiveList.addAll(weaponClass.getMainWeapon().getPassiveList());
            }
        }
        
        for(Passive passive : Passive.values()){
            int size = (int) passiveList.stream().filter(p -> p == passive).count();
    
            float influence = 1.0F;
            if(size != 0){
                float sum = 0.0F;
                for(int i = 1; i <= size; i++){
                    sum += passive.getInfluence() / (passive.getMulti_influence_rate() * (float) size);
                }
                influence = 1.0F + sum;
            }
            
            passiveInfluenceMap.put(passive, influence);
        }
    }
    
    /**
     * 指定されたパッシブの影響倍率を取得します
     * @param passive 取得したいパッシブ効果
     * @return パッシブの影響倍率
     */
    public float getInfluence(Passive passive){
        return passiveInfluenceMap.get(passive);
    }
}
