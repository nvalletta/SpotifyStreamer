package nanodegree.spotifystreamer.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.activities.SpotifyActivity;
import nanodegree.spotifystreamer.models.Session;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.MusicPlayerService;

public final class MusicPlayerDialogFragment extends DialogFragment implements Observer, SeekBar.OnSeekBarChangeListener {

    public static final String CHOSEN_ARTIST_KEY = "CHOSEN_ARTIST";
    public static final String CHOSEN_TRACK_KEY = "CHOSEN_TRACK";
    public static final String CHOSEN_TRACK_INDEX = "CHOSEN_TRACK_INDEX";

    private MusicPlayerService musicPlayerService;
    private boolean isBound;
    private boolean autoPlay = true;

    private SpotifyArtist artist;
    private SpotifyTrack track;
    private int trackIndex;

    private boolean lockPlayCommands = false;

    private ImageButton previousButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;

    private SeekBar seekBar;


    private void resetDurationAndElapsedTime() {
        TextView trackDuration = (TextView)getView().findViewById(R.id.end_duration);
        TextView elapsedTime = (TextView)getView().findViewById(R.id.start_duration);

        trackDuration.setText("0:00");
        elapsedTime.setText("0:00");
        seekBar.setProgress(0);
    }


    private void populateViewData(View rootView) {
        TextView artistName = (TextView)rootView.findViewById(R.id.artistName);
        TextView albumName = (TextView)rootView.findViewById(R.id.albumName);
        TextView trackName = (TextView)rootView.findViewById(R.id.trackName);

        if (null != artist) {
            artistName.setText(artist.getName());
        }
        albumName.setText(track.getAlbumName());
        trackName.setText(track.getTrackName());

        ImageView albumImage = (ImageView)rootView.findViewById(R.id.albumImage);
        Picasso.with(this.getActivity()).load(track.getAlbumThumbnailUrl()).into(albumImage);
    }


    private void setPlayButtonVisibilityBasedOnPauseState(boolean isPaused) {
        if (isPaused) {
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        } else {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }


    private void navigatePrevious() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            trackIndex--;
            if (trackIndex < 0) {
                trackIndex = SpotifyActivity.artistTracks.size() - 1;
            }
            track = SpotifyActivity.artistTracks.get(trackIndex);
            musicPlayerService.playTrack(track);
            populateViewData(getView());
            lockPlayCommands = false;

            resetDurationAndElapsedTime();
        }
    }


    private void navigateNext() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            trackIndex++;
            if (trackIndex >= SpotifyActivity.artistTracks.size()) {
                trackIndex = 0;
            }
            track = SpotifyActivity.artistTracks.get(trackIndex);
            musicPlayerService.playTrack(track);
            populateViewData(getView());
            lockPlayCommands = false;

            resetDurationAndElapsedTime();
        }
    }


    private void playSong() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            musicPlayerService.playTrack(track);
            setPlayButtonVisibilityBasedOnPauseState(false);
            lockPlayCommands = false;
        }
    }


    private void pauseSong() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            musicPlayerService.pauseTrack();
            setPlayButtonVisibilityBasedOnPauseState(true);
            lockPlayCommands = false;
        }
    }


    private void setUpButtonListeners() {
        this.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatePrevious();
            }
        });

        this.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        this.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pauseSong();
            }
        });

        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateNext();
            }
        });

        this.seekBar.setOnSeekBarChangeListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_player, container, false);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));

        this.previousButton = (ImageButton)view.findViewById(R.id.previousButton);
        this.playButton = (ImageButton)view.findViewById(R.id.playButton);
        this.pauseButton = (ImageButton)view.findViewById(R.id.pauseButton);
        this.nextButton = (ImageButton)view.findViewById(R.id.nextButton);

        this.seekBar = (SeekBar)view.findViewById(R.id.seekBar);

        this.setUpButtonListeners();

        if (getArguments() == null) {
            if (MusicPlayerService.musicSession != null) {
                this.artist = MusicPlayerService.musicSession.artist;
                this.track = MusicPlayerService.musicSession.track;
                this.trackIndex = MusicPlayerService.musicSession.trackIndex;
            }
        } else {
            this.artist = getArguments().getParcelable(CHOSEN_ARTIST_KEY);
            this.track = getArguments().getParcelable(CHOSEN_TRACK_KEY);
            this.trackIndex = getArguments().getInt(CHOSEN_TRACK_INDEX);
        }

        if (SpotifyActivity.chosenTrackIndex > -1) {
            if (SpotifyActivity.chosenTrackIndex == this.trackIndex) {
                autoPlay = false;
            }
        }

        SpotifyActivity.chosenTrackIndex = this.trackIndex;

        if (null != this.track) {
            populateViewData(view);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this.getActivity(), MusicPlayerService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
        if (MusicPlayerService.musicSession != null) {
            MusicPlayerService.musicSession.addObserver(this);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
        if (MusicPlayerService.musicSession != null) {
            MusicPlayerService.musicSession.deleteObserver(this);
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            musicPlayerService = binder.getService();
            if (autoPlay) {
                musicPlayerService.playTrack(track);
                setPlayButtonVisibilityBasedOnPauseState(false);
            } else {
                if (null != musicPlayerService) {
                    setPlayButtonVisibilityBasedOnPauseState(!musicPlayerService.songIsPlaying());
                }
            }

            if (MusicPlayerService.musicSession != null && MusicPlayerService.musicSession.countObservers() == 0) {
                MusicPlayerService.musicSession.addObserver(MusicPlayerDialogFragment.this);
            }

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };


    @Override
    public void update(Observable observable, Object data) {
        final Session session = (Session)observable;

        if (musicPlayerService != null && !musicPlayerService.songIsPlaying()) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView trackDuration = (TextView)getView().findViewById(R.id.end_duration);
                TextView elapsedTime = (TextView)getView().findViewById(R.id.start_duration);

                if (trackDuration.getText().equals("0:00")) {
                    trackDuration.setText(String.format("0:%02d", (session.getDuration() / 1000)));
                    seekBar.setMax(session.getDuration() / 1000);
                }
                elapsedTime.setText(String.format("0:%02d", session.getElapsedSeconds()));
                seekBar.setProgress(session.getElapsedSeconds());
            }
        });

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (null != musicPlayerService) {
            musicPlayerService.seekTrack(progress);
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (null != musicPlayerService) {
            musicPlayerService.pauseTrack();
        }
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (null != musicPlayerService) {
            musicPlayerService.playTrack(track);
        }
    }
}
