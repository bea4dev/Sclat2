package be4rjp.sclat2.player.death;

import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;

public class PlayerDeathManager {

    /**
     * プレイヤーの死亡演出
     * @param target 死んだプレイヤー
     * @param killer キルしたプレイヤー、存在しない場合はnull若しくはsclatPlayerと同じ物を指定
     * @param sclatWeapon キルするのに使用した武器、なければnullを指定
     * @param deathType 死亡の種類
     */
    public static void death(SclatPlayer target, SclatPlayer killer, SclatWeapon sclatWeapon, DeathType deathType){
        if(killer == null) killer = target;

        SclatTeam sclatTeam = target.getSclatTeam();
        if(sclatTeam == null) return;
        Match match = sclatTeam.getMatch();

        //テキスト系
        switch (deathType){
            case KILLED_BY_PLAYER:{
                for(SclatPlayer matchPlayer : match.getPlayers()){
                    if(matchPlayer == target || matchPlayer == killer){

                    }else{
                        matchPlayer.sendText("match-killed-title", killer.getDisplayName(), sclatWeapon.getDisplayName(matchPlayer.getLang()));
                    }
                }
                break;
            }
        }
    }
}
