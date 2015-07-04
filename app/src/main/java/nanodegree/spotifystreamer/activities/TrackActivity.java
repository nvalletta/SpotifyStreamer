package nanodegree.spotifystreamer.activities;


import android.app.Activity;
import android.os.Bundle;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.fragments.ArtistActivityFragment;
import nanodegree.spotifystreamer.models.SpotifyArtist;

public final class TrackActivity extends Activity {

    private SpotifyArtist spotifyArtist = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);


        if (null != getIntent() && getIntent().hasExtra(ArtistActivityFragment.ARTIST_PARCEL_KEY)) {
            spotifyArtist = getIntent().getParcelableExtra(ArtistActivityFragment.ARTIST_PARCEL_KEY);
        }
    }


}
