package be4rjp.sclat2.match.intro;

import be4rjp.cinema4c.data.play.MovieData;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.map.SclatMap;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IntroManager {

    private static Map<Integer, Match> matchMap = new ConcurrentHashMap<>();

    public static Match getMatchByMoviePlayID(int playID){
        return matchMap.get(playID);
    }

    private static void registerMatch(Match match, int playID){
        matchMap.put(playID, match);
    }


    public static void playIntro(Match match){
        SclatMap sclatMap = match.getSclatMap();
        MovieData movieData = sclatMap.getIntroMovie();
        if(movieData == null) return;

        Set<Player> players = new HashSet<>();
        match.getPlayers().stream()
                .filter(sclatPlayer -> sclatPlayer.getBukkitPlayer() != null)
                .forEach(sclatPlayer -> players.add(sclatPlayer.getBukkitPlayer()));
        int playID = movieData.play(players);
        registerMatch(match, playID);
    }
}
