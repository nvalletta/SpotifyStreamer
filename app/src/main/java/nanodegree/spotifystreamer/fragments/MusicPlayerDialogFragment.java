package nanodegree.spotifystreamer.fragments;


import android.os.Bundle;
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

public final class MusicPlayerDialogFragment extends DialogFragment {

    public static final String CHOSEN_ARTIST_KEY = "CHOSEN_ARTIST";
    public static final String CHOSEN_TRACK_KEY = "CHOSEN_TRACK";

    private SpotifyArtist artist;
    private SpotifyTrack track;

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


    private void setUpButtonListeners() {
        this.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: previous track.
            }
        });

        this.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SpotifyActivity)getActivity()).playTrack(track);
            }
        });

        this.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: pause music.
            }
        });

        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: next track.
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
        if (null != this.track) {
            populateViewData(view);
        }

        return view;
    }

}
