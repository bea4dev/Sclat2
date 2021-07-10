package be4rjp.sclat2.match.team;

import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.player.SclatPlayer;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * チーム
 */
public class SclatTeam {
    
    //試合のインスタンス
    private final Match match;
    //チームカラー
    private final SclatColor sclatColor;
    //チームの塗りポイント
    private int paints = 0;
    //チームのキルカウント
    private int kills = 0;
    //チームメンバー
    private Set<SclatPlayer> teamMembers = new ConcurrentSet<>();
    //スコアボードのチーム
    private final Team team;
    
    /**
     * チームのインスタンス作成
     * @param match
     * @param sclatColor
     */
    public SclatTeam(Match match, SclatColor sclatColor){
        this.match = match;
        this.sclatColor = sclatColor;
        
        this.team = match.getScoreboard().getBukkitScoreboard().registerNewTeam(sclatColor.getDisplayName());
        team.setColor(sclatColor.getChatColor());
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setSuffix(sclatColor.getChatColor().toString());
        team.setCanSeeFriendlyInvisibles(true);
        team.setDisplayName(sclatColor.getDisplayName());
        
        this.match.addSclatTeam(this);
    }
    
    
    public int getKills() {return kills;}
    
    public int getPaints() {return paints;}
    
    public synchronized void addPaints(int paints) {this.paints += paints;}
    
    public synchronized void addKills(int kills) {this.kills += kills;}
    
    public Match getMatch() {return match;}
    
    public SclatColor getSclatColor() {return sclatColor;}
    
    public Team getScoreBoardTeam() {return team;}
    
    /**
     * プレイヤーをチームに参加させる
     * @param sclatPlayer 参加させるプレイヤー
     */
    public void join(SclatPlayer sclatPlayer){
        if(sclatPlayer.getBukkitPlayer() != null){
            Player player = sclatPlayer.getBukkitPlayer();
            //player.setScoreboard(match.getScoreboard().getBukkitScoreboard());
            team.addEntry(player.getName());
        }
        sclatPlayer.setSclatTeam(this);
        teamMembers.add(sclatPlayer);
        sclatPlayer.sendText("match-join");
    }

    /**
     * 同じ試合のほかのチームを取得する
     * @return Set<SclatTeam>
     */
    public Set<SclatTeam> getOtherTeam(){
        Set<SclatTeam> sclatTeams = new HashSet<>();
        for(SclatTeam sclatTeam : match.getSclatTeams()){
            if(sclatTeam != this) sclatTeams.add(sclatTeam);
        }
        return sclatTeams;
    }
    
    /**
     * 他のチームのプレイヤーを取得する
     * @return Set<SclatPlayer>
     */
    public Set<SclatPlayer> getOtherTeamPlayers(){
        Set<SclatPlayer> players = new HashSet<>();
        this.getOtherTeam().forEach(sclatTeam -> players.addAll(sclatTeam.getTeamMembers()));
        return players;
    }

    /**
     * チームメンバーを取得します
     * @return Set<SclatPlayer>
     */
    public Set<SclatPlayer> getTeamMembers(){
        return teamMembers;
    }
}
