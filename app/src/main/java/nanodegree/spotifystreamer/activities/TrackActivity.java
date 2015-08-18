package nanodegree.spotifystreamer.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.fragments.ArtistActivityFragment;
import nanodegree.spotifystreamer.models.SpotifyArtist;


public final class TrackActivity extends SpotifyActivity {


    private static final String SPOTIFY_ARTIST_PARCEL_KEY = "SELECTED_ARTIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (null != savedInstanceState && null != savedInstanceState.getParcelable(SPOTIFY_ARTIST_PARCEL_KEY)) {
            SpotifyActivity.chosenArtist = savedInstanceState.getParcelable(SPOTIFY_ARTIST_PARCEL_KEY);
        }

        populateViewWithArtistTrackData();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(SPOTIFY_ARTIST_PARCEL_KEY, SpotifyActivity.chosenArtist);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void populateViewWithArtistTrackData() {
        if (null != SpotifyActivity.chosenArtist) {
            TextView artistName = (TextView)findViewById(R.id.mainText);
            artistName.setText(SpotifyActivity.chosenArtist.getName());
            retrieveTopTracks(SpotifyActivity.chosenArtist.getId());
        }
    }


}
