package nanodegree.spotifystreamer.activities;

import android.app.Activity;
import android.content.Intent;

import nanodegree.spotifystreamer.services.ArtistRetrievalService;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public abstract class SpotifyActivity extends Activity {

    public void performArtistSearch(String query) {
        Intent searchIntent = new Intent(SpotifyActivity.this, ArtistRetrievalService.class);
        searchIntent.putExtra(ArtistRetrievalService.ARTIST_QUERY_INTENT_KEY, query);
        SpotifyActivity.this.startService(searchIntent);
    }

    public void retrieveTopTracks(String artistId) {
        Intent topTracksIntent = new Intent(this, TopTrackRetrievalService.class);
        topTracksIntent.putExtra(TopTrackRetrievalService.ARTIST_ID_INTENT_KEY, artistId);
        startService(topTracksIntent);
    }

}
