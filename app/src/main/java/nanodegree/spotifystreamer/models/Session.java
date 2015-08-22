package nanodegree.spotifystreamer.models;

import android.os.Handler;

import java.util.Observable;


public class Session extends Observable {
    public SpotifyArtist artist;
    public SpotifyTrack track;
    public int trackIndex;

    private int trackDuration = 0;

    Handler timer = new Handler();
    Runnable timeElapseEvent = new Runnable() {
        @Override
        public void run() {
            Session.this.setChanged();
            Session.this.notifyObservers();
            timer.postDelayed(timeElapseEvent, 1000);
        }
    };

    private static Session session;

    public static Session getSession() {
        if (null == session) {
            session = new Session();
        }
        return session;
    }

    public void startTimer() {
        timer = new Handler();
        timer.postDelayed(timeElapseEvent, 1000);
    }

    public void stopTimer() {
        timer.removeCallbacks(timeElapseEvent);
    }

    public void setDuration(int duration) {
        this.trackDuration = duration;
        setChanged();
        notifyObservers();
    }

    public int getDuration() {
        return trackDuration;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.setChanged();
        this.notifyObservers();
    }

    public void setTrack(SpotifyTrack track) {
        this.track = track;
    }
}