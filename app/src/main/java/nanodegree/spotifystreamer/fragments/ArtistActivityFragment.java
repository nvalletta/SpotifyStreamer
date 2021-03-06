package nanodegree.spotifystreamer.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.activities.SpotifyActivity;
import nanodegree.spotifystreamer.activities.TrackActivity;
import nanodegree.spotifystreamer.adapters.ArtistListAdapter;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.services.ArtistRetrievalService;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public final class ArtistActivityFragment extends ListFragment {


    public static final String SPOTIFY_ARTISTS_PARCEL_KEY = "ARTIST_LIST";
    public static final String ARTIST_PARCEL_KEY = "SELECTED_ARTIST";

    private List<SpotifyArtist> spotifyArtists = new ArrayList<>();
    private ArtistListAdapter mAdapter;
    private final BroadcastReceiver artistSearchBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            spotifyArtists = intent.getParcelableArrayListExtra(ArtistRetrievalService.ARTISTS_EXTRA_KEY);
            mAdapter = new ArtistListAdapter(getActivity(), spotifyArtists);
            setListAdapter(mAdapter);
            if (spotifyArtists.isEmpty()) {
                Toast.makeText(getActivity(), context.getString(R.string.no_results_found_please_refine_your_search), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void launchTrackActivity(SpotifyArtist chosenArtist) {
        Intent trackActivityIntent = new Intent(getActivity().getApplicationContext(), TrackActivity.class);
        trackActivityIntent.putExtra(ARTIST_PARCEL_KEY, chosenArtist);
        getActivity().startActivity(trackActivityIntent);
    }


    private void retrieveTopTracksForArtist(SpotifyArtist chosenArtist) {
        Intent topTracksIntent = new Intent(getActivity(), TopTrackRetrievalService.class);
        topTracksIntent.putExtra(TopTrackRetrievalService.ARTIST_ID_INTENT_KEY, chosenArtist.getId());
        getActivity().startService(topTracksIntent);
    }


    public ArtistActivityFragment() { }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState && null != savedInstanceState.getParcelableArrayList(SPOTIFY_ARTISTS_PARCEL_KEY)) {
            spotifyArtists = savedInstanceState.getParcelableArrayList(SPOTIFY_ARTISTS_PARCEL_KEY);
        }
        mAdapter = new ArtistListAdapter(getActivity(), spotifyArtists);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SPOTIFY_ARTISTS_PARCEL_KEY, (ArrayList<? extends Parcelable>)spotifyArtists);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ArtistRetrievalService.ARTIST_BROADCAST_FILTER);
        getActivity().registerReceiver(artistSearchBroadcastReceiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(artistSearchBroadcastReceiver);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String tagName = getActivity().getString(R.string.track_fragment_tag);
        Fragment fragment = getActivity().getFragmentManager().findFragmentByTag(tagName);

        SpotifyActivity.chosenTrackIndex = -1;
        SpotifyActivity.artistTracks.clear();

        SpotifyArtist chosenArtist = mAdapter.getSpotifyArtistAtIndex(position);
        SpotifyActivity.chosenArtist = chosenArtist;
        if (null != fragment) {
            retrieveTopTracksForArtist(chosenArtist);
        } else {
            launchTrackActivity(chosenArtist);
        }
    }
}
