package be4rjp.sclat2.match;

import be4rjp.sclat2.match.team.SclatTeam;

public class PlayerLobbyMatch extends Match{
    @Override
    public boolean checkWin() {
        return false;
    }

    @Override
    public SclatTeam getWinner() {
        return null;
    }
}
