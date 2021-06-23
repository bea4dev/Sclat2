package be4rjp.sclat2.player.death;

import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.team.SclatTeam;
import be4rjp.sclat2.player.SclatPlayer;
import be4rjp.sclat2.weapon.SclatWeapon;
import org.bukkit.GameMode;
import org.bukkit.Location;

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
        target.setDeath(true);
        target.setGameMode(GameMode.SPECTATOR);
        Match match = sclatTeam.getMatch();

        //チャットテキスト系
        switch (deathType){
            case KILLED_BY_PLAYER:{
                for(SclatPlayer matchPlayer : match.getPlayers()){
                    if(matchPlayer == target || matchPlayer == killer){
                        matchPlayer.sendText("match-kill-message-bold", killer.getDisplayName(true), target.getDisplayName(true), sclatWeapon.getDisplayName(matchPlayer.getLang()));
                    }else{
                        matchPlayer.sendText("match-kill-message", killer.getDisplayName(), target.getDisplayName(), sclatWeapon.getDisplayName(matchPlayer.getLang()));
                    }
                }
                break;
            }
            
            case FELL_INTO_VOID:{
                match.getPlayers().forEach(sclatPlayer -> sclatPlayer.sendText("match-fall-void-message", target.getDisplayName()));
                break;
            }
        }
    
        int index = match.getSclatTeams().indexOf(sclatTeam);
        Location respawnLocation = match.getSclatMap().getTeamLocation(index);
        new PlayerDeathRunnable(respawnLocation, target, killer, sclatWeapon, deathType).start();
    }
}
