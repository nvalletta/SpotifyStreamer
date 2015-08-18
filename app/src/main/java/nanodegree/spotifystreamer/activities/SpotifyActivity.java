package nanodegree.spotifystreamer.activities;

import android.content.Intent;
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
import nanodegree.spotifystreamer.services.MusicPlayerService;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public abstract class SpotifyActivity extends FragmentActivity {


    public static SpotifyArtist chosenArtist;
    public static List<SpotifyTrack> artistTracks = new ArrayList<>();
    public static int chosenTrackIndex;


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


    public static void launchMusicPlayerDialogFragment(FragmentManager fm) {
        MusicPlayerDialogFragment musicPlayerDialogFragment = new MusicPlayerDialogFragment();

        Bundle argument = new Bundle();
        argument.putParcelable(MusicPlayerDialogFragment.CHOSEN_ARTIST_KEY, chosenArtist);
        argument.putParcelable(MusicPlayerDialogFragment.CHOSEN_TRACK_KEY, artistTracks.get(chosenTrackIndex));
        argument.putInt(MusicPlayerDialogFragment.CHOSEN_TRACK_INDEX, chosenTrackIndex);
        musicPlayerDialogFragment.setArguments(argument);
        musicPlayerDialogFragment.show(fm, "player");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (null != chosenArtist && null != SpotifyActivity.artistTracks.get(chosenTrackIndex)) {
            launchMusicPlayerDialogFragment(getSupportFragmentManager());
        }

        return super.onOptionsItemSelected(item);
    }

}
