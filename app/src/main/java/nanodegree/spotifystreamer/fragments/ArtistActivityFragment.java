package nanodegree.spotifystreamer.fragments;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.internal.bind.MapTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.activities.TrackActivity;
import nanodegree.spotifystreamer.adapters.ArtistListAdapter;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import nanodegree.spotifystreamer.services.ArtistRetrievalService;


public final class ArtistActivityFragment extends ListFragment {


    public static final String ARTIST_PARCEL_KEY = "SELECTED_ARTIST";

    private ArtistListAdapter mAdapter;
    private BroadcastReceiver artistSearchBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<SpotifyArtist> spotifyArtists = intent.getParcelableArrayListExtra(ArtistRetrievalService.ARTISTS_EXTRA_KEY);
            mAdapter = new ArtistListAdapter(getActivity(), spotifyArtists);
            setListAdapter(mAdapter);
        }
    };


    public ArtistActivityFragment() { }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<SpotifyArtist> spotifyArtists = new ArrayList<>();
        mAdapter = new ArtistListAdapter(getActivity(), spotifyArtists);
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
        Intent trackActivityIntent = new Intent(getActivity().getApplicationContext(), TrackActivity.class);
        trackActivityIntent.putExtra(ARTIST_PARCEL_KEY, mAdapter.getSpotifyArtistAtIndex(position));
        getActivity().startActivity(trackActivityIntent);
    }
}
