package be4rjp.sclat2.match;

import be4rjp.sclat2.match.map.SclatMap;
import be4rjp.sclat2.match.team.SclatTeam;

public class PlayerLobbyMatch extends Match{
    public PlayerLobbyMatch(SclatMap sclatMap) {
        super(sclatMap);
    }
    
    @Override
    public MatchType getType() {
        return MatchType.LOBBY;
    }
    
    @Override
    public void initialize() {
    
    }
    
    @Override
    public boolean checkWin() {
        return false;
    }

    @Override
    public SclatTeam getWinner() {
        return null;
    }
}
