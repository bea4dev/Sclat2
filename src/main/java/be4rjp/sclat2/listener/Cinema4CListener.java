package be4rjp.sclat2.listener;

import be4rjp.cinema4c.event.AsyncMoviePlayFinishEvent;
import be4rjp.sclat2.match.Match;
import be4rjp.sclat2.match.intro.IntroManager;
import be4rjp.sclat2.match.intro.ReadyRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Cinema4CListener implements Listener {

    @EventHandler
    public void onFinishMovie(AsyncMoviePlayFinishEvent event){
        int playID = event.getPlayID();

        Match match = IntroManager.getMatchByMoviePlayID(playID);
        if(match != null){
            ReadyRunnable readyRunnable = new ReadyRunnable(match);
            readyRunnable.start();
        }
    }
}
