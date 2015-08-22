package nanodegree.spotifystreamer.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.fragments.MusicPlayerDialogFragment;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.ArtistRetrievalService;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public abstract class SpotifyActivity extends FragmentActivity {


    private static final String DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG";

    public static SpotifyArtist chosenArtist;
    public static List<SpotifyTrack> artistTracks = new ArrayList<>();
    public static int chosenTrackIndex = -1;

    public static boolean deviceHasSmallScreen = true;


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


    public void launchMusicDialog() {
        if (!deviceHasSmallScreen) {
            FragmentManager fm = getSupportFragmentManager();
            MusicPlayerDialogFragment dialog = new MusicPlayerDialogFragment();
            dialog.show(fm, DIALOG_FRAGMENT_TAG);
        } else {
            Intent musicActivityIntent = new Intent(this, MusicFragmentActivity.class);
            startActivity(musicActivityIntent);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        int screenConfig = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        deviceHasSmallScreen = (screenConfig == Configuration.SCREENLAYOUT_SIZE_NORMAL
                ||  screenConfig == Configuration.SCREENLAYOUT_SIZE_SMALL);

        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (-1 == chosenTrackIndex) {
            return super.onOptionsItemSelected(item);
        }
        if (null != SpotifyActivity.artistTracks.get(chosenTrackIndex) && getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            launchMusicDialog();
        }

        return super.onOptionsItemSelected(item);
    }

}
