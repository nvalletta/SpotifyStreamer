package nanodegree.spotifystreamer.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.fragments.ArtistActivityFragment;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;

public final class TrackActivity extends Activity {


    private static final String SPOTIFY_ARTIST_PARCEL_KEY = "SELECTED_ARTIST";
    private SpotifyArtist spotifyArtist = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (null != savedInstanceState && null != savedInstanceState.getParcelable(SPOTIFY_ARTIST_PARCEL_KEY)) {
            spotifyArtist = savedInstanceState.getParcelable(SPOTIFY_ARTIST_PARCEL_KEY);
            populateViewWithArtistTrackData();
            return;
        }

        if (null != getIntent() && getIntent().hasExtra(ArtistActivityFragment.ARTIST_PARCEL_KEY) && null == spotifyArtist) {
            spotifyArtist = getIntent().getParcelableExtra(ArtistActivityFragment.ARTIST_PARCEL_KEY);
            if (null != spotifyArtist) {
                populateViewWithArtistTrackData();
                retrieveTopTracks();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(SPOTIFY_ARTIST_PARCEL_KEY, spotifyArtist);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void populateViewWithArtistTrackData() {
        if (null != spotifyArtist) {
            TextView artistName = (TextView)findViewById(R.id.mainText);
            artistName.setText(spotifyArtist.getName());
        }
    }


    private void retrieveTopTracks() {
        Intent topTracksIntent = new Intent(this, TopTrackRetrievalService.class);
        topTracksIntent.putExtra(TopTrackRetrievalService.ARTIST_ID_INTENT_KEY, spotifyArtist.getId());
        startService(topTracksIntent);
    }


}
