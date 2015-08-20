package nanodegree.spotifystreamer.models;

import android.os.Handler;

import java.util.Observable;


public class Session extends Observable {
    public SpotifyArtist artist;
    public SpotifyTrack track;
    public int trackIndex;

    private int trackDuration = 0;
    public int elapsedSeconds = 0;

    Handler timer = new Handler();
    Runnable timeElapseEvent = new Runnable() {
        @Override
        public void run() {
            elapsedSeconds++;
            Session.this.setChanged();
            Session.this.notifyObservers();
            timer.postDelayed(timeElapseEvent, 1000);
        }
    };

    public void resumeTimer() {
        timer = new Handler();
        timer.postDelayed(timeElapseEvent, 1000);
    }

    public void startTimer() {
        elapsedSeconds = 0;
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

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
        this.setChanged();
        this.notifyObservers();
    }

    public void setTrack(SpotifyTrack track) {
        this.track = track;
        this.elapsedSeconds = 0;
    }
}