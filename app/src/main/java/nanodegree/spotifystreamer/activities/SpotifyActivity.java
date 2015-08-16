package nanodegree.spotifystreamer.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import nanodegree.spotifystreamer.fragments.MusicPlayerDialogFragment;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.ArtistRetrievalService;
import nanodegree.spotifystreamer.services.MusicPlayerService;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public abstract class SpotifyActivity extends FragmentActivity {

    public static SpotifyArtist chosenArtist;
    public static SpotifyTrack chosenTrack;

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

    public void playTrack(SpotifyTrack track) {
        Intent topTracksIntent = new Intent(this, MusicPlayerService.class);
        topTracksIntent.putExtra(MusicPlayerService.TRACK_URI_INTENT_KEY, track.getPreviewUrl());
        topTracksIntent.setAction(MusicPlayerService.ACTION_PLAY);
        startService(topTracksIntent);
    }

    public void pauseTrack() {
        Intent topTracksIntent = new Intent(this, MusicPlayerService.class);
        topTracksIntent.setAction(MusicPlayerService.ACTION_PAUSE);
        startService(topTracksIntent);
    }

}
