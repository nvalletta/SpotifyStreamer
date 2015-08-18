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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.activities.SpotifyActivity;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.MusicPlayerService;

public final class MusicPlayerDialogFragment extends DialogFragment {

    public static final String CHOSEN_ARTIST_KEY = "CHOSEN_ARTIST";
    public static final String CHOSEN_TRACK_KEY = "CHOSEN_TRACK";
    public static final String CHOSEN_TRACK_INDEX = "CHOSEN_TRACK_INDEX";

    private MusicPlayerService musicPlayerService;
    private boolean isBound;

    private SpotifyArtist artist;
    private SpotifyTrack track;
    private int trackIndex;

    private boolean lockPlayCommands = false;

    private ImageButton previousButton;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton nextButton;


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


    private void navigatePrevious() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            trackIndex--;
            if (trackIndex < 0) {
                trackIndex = SpotifyActivity.artistTracks.size() - 1;
            }
            track = SpotifyActivity.artistTracks.get(trackIndex);
            musicPlayerService.playTrack(track.getPreviewUrl());
            lockPlayCommands = false;
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
            musicPlayerService.playTrack(track.getPreviewUrl());
            lockPlayCommands = false;
        }
    }


    private void playSong() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            if (musicPlayerService.playTrack(track.getPreviewUrl())) {
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
            lockPlayCommands = false;
        }
    }


    private void pauseSong() {
        if (isBound && !lockPlayCommands) {
            lockPlayCommands = true;
            musicPlayerService.pauseTrack();
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_player, container);

        this.previousButton = (ImageButton)view.findViewById(R.id.previousButton);
        this.playButton = (ImageButton)view.findViewById(R.id.playButton);
        this.pauseButton = (ImageButton)view.findViewById(R.id.pauseButton);
        this.nextButton = (ImageButton)view.findViewById(R.id.nextButton);

        this.setUpButtonListeners();

        this.artist = getArguments().getParcelable(CHOSEN_ARTIST_KEY);
        this.track = getArguments().getParcelable(CHOSEN_TRACK_KEY);
        this.trackIndex = getArguments().getInt(CHOSEN_TRACK_INDEX);

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
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            musicPlayerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };


}
