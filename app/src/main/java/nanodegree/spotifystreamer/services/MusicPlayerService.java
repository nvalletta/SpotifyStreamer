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


public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {


    public static final String TRACK_DURATION_INTENT_FILTER = "TRACK_DURATION_INTENT_FILTER";
    public static final String PLAY_PAUSE_READY_INTENT_FILTER = "PLAY_PAUSE_READY_INTENT_FILTER";

    public static final String TRACK_DURATION_INTENT_KEY = "TRACK_DURATION_INTENT_KEY";

    public static final String TRACK_URI_INTENT_KEY = "TRACK_URI";

    public static final String ACTION_PLAY = "PLAY_TRACK";
    public static final String ACTION_PAUSE = "PAUSE_TRACK";

    private final IBinder localBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    private String currentTrackUri;


    private void emitDurationIntent() {
        //TODO: Tell the music player dialog the duration of the song.
    }


    private void emitPlayPauseReadyIntent() {
        //TODO: Tell the music player dialog that it can switch icons now.
    }


    private void acquireLocks() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
    }


    private boolean setMediaDataSource(String trackUri) {
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(trackUri));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void prepareMediaPlayer() {
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
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
        emitDurationIntent();
        emitPlayPauseReadyIntent();
        mp.start();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public boolean playTrack(String trackUri) {
        if (null == trackUri) {
            return false;
        }
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        }

        if (mediaPlayer.isPlaying()) {
            if (trackUri.equals(currentTrackUri)) {
                return true;
            }
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (setMediaDataSource(trackUri)) {
                prepareMediaPlayer();
                return true;
            }
        }

        return false;
    }


    public void pauseTrack() {
        if (null != mediaPlayer) {
            mediaPlayer.pause();
        }
    }


    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            // Return this instance of MusicPlayerService so clients can call public methods
            return MusicPlayerService.this;
        }
    }

}
