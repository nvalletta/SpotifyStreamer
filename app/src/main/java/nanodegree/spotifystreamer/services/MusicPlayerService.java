package nanodegree.spotifystreamer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

import nanodegree.spotifystreamer.activities.SpotifyActivity;
import nanodegree.spotifystreamer.models.Session;
import nanodegree.spotifystreamer.models.SpotifyTrack;


public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {


    private final IBinder localBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    private String currentTrackUri;

    public static Session musicSession = new Session();


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
        musicSession.setDuration(mp.getDuration());
        musicSession.startTimer();
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
        String trackUri = trackToPlay.getPreviewUrl();
        if (null == mediaPlayer) {
            musicSession = new Session();
            musicSession.artist = SpotifyActivity.chosenArtist;
            musicSession.setTrack(trackToPlay);
            musicSession.trackIndex = SpotifyActivity.chosenTrackIndex;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        }

        if (mediaPlayer.isPlaying() && !trackUri.equals(currentTrackUri)) {
            musicSession.stopTimer();
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        } else {
            if (trackUri.equals(currentTrackUri)) {
                musicSession.resumeTimer();
                mediaPlayer.start();
            } else {
                musicSession.stopTimer();
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
            musicSession.stopTimer();
            mediaPlayer.pause();
        }
    }


    public void seekTrack(int progress) {
        if (null != mediaPlayer) {
            musicSession.setElapsedSeconds(progress);
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
        musicSession.stopTimer();
    }


    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

}
