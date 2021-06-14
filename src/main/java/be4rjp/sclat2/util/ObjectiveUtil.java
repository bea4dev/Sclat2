package be4rjp.sclat2.util;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.List;

public class ObjectiveUtil {
    public static void setLine(Objective objective, List<String> list){
        int index = list.size();
        
        for(String line : list){
            index -= 1;
            Score score = objective.getScore(line);
            score.setScore(index);
        }
    }
}
