package nanodegree.spotifystreamer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

import java.io.IOException;


public class MusicPlayerService extends IntentService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {


    public static final String TRACK_DURATION_INTENT_FILTER = "TRACK_DURATION_INTENT_FILTER";
    public static final String PLAY_PAUSE_READY_INTENT_FILTER = "PLAY_PAUSE_READY_INTENT_FILTER";

    public static final String TRACK_DURATION_INTENT_KEY = "TRACK_DURATION_INTENT_KEY";

    public static final String TRACK_URI_INTENT_KEY = "TRACK_URI";

    public static final String ACTION_PLAY = "PLAY_TRACK";
    public static final String ACTION_PAUSE = "PAUSE_TRACK";

    private MediaPlayer mediaPlayer;
    private Uri trackPreviewUri;


    public MusicPlayerService() { super(""); }


    public MusicPlayerService(String name) {
        super(name);
    }


    private void emitDurationIntent() {
        Intent durationIntent = new Intent(TRACK_DURATION_INTENT_FILTER);
        durationIntent.putExtra()
        sendBroadcast(durationIntent);
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


    private boolean setMediaDataSource() {
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), trackPreviewUri);
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


    private void handleIntentForAction(String action) {
        if (action.equals(ACTION_PLAY)) {
            mediaPlayer = new MediaPlayer();
            if (null != trackPreviewUri && setMediaDataSource()) {
                prepareMediaPlayer();
            }
        } else if (action.equals(ACTION_PAUSE)) {
            //TODO: pause.
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == trackPreviewUri) {
            trackPreviewUri = Uri.parse(intent.getStringExtra(TRACK_URI_INTENT_KEY));
        }
        if (null != intent.getAction()) {
            handleIntentForAction(intent.getAction());
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            trackPreviewUri = null;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

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
}
