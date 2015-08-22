package nanodegree.spotifystreamer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

import nanodegree.spotifystreamer.activities.SpotifyActivity;
import nanodegree.spotifystreamer.models.Session;
import nanodegree.spotifystreamer.models.SpotifyTrack;


public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {


    private final IBinder localBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;

    public static String currentTrackUri = "";


    private void acquireLocks() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
    }


    private boolean setMediaDataSource(String trackUri) {
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(trackUri));
            currentTrackUri = trackUri;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void prepareMediaPlayer() {
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        acquireLocks();
        mediaPlayer.prepareAsync();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        Session.getSession().setDuration(mp.getDuration());
        Session.getSession().startTimer();
        mp.start();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public boolean playTrack(SpotifyTrack trackToPlay) {
        if (null == trackToPlay) {
            return false;
        }
        String trackUri = trackToPlay.getPreviewUri();
        if (null == mediaPlayer) {
            Session.getSession().artist = SpotifyActivity.chosenArtist;
            Session.getSession().setTrack(trackToPlay);
            Session.getSession().trackIndex = SpotifyActivity.chosenTrackIndex;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        }

        if (mediaPlayer.isPlaying() && !trackUri.equals(currentTrackUri)) {
            Session.getSession().stopTimer();
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        } else {
            if (trackUri.equals(currentTrackUri)) {
                mediaPlayer.start();
                Session.getSession().resumeTimer();
            } else {
                Session.getSession().stopTimer();
                mediaPlayer.reset();
                if (setMediaDataSource(trackUri)) {
                    prepareMediaPlayer();
                    return true;
                }
            }
        }

        return false;
    }


    public void pauseTrack() {
        if (null != mediaPlayer) {
            Session.getSession().stopTimer();
            mediaPlayer.pause();
        }
    }


    public void seekTrack(int progress) {
        if (null != mediaPlayer) {
            Session.getSession().setElapsedSeconds(progress);
            mediaPlayer.seekTo(progress*1000);
        }
    }


    public boolean songIsPlaying() {
        if (null != mediaPlayer) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Session.getSession().stopTimer();
    }


    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

}
