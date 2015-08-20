package nanodegree.spotifystreamer.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import nanodegree.spotifystreamer.adapters.TrackListAdapter;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public final class TrackActivityFragment extends ListFragment {


    private static final String SPOTIFY_TRACKS_PARCEL_KEY = "SPOTIFY_TRACKS";
    private TrackListAdapter mAdapter;
    private final BroadcastReceiver topTracksBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SpotifyActivity.artistTracks = intent.getParcelableArrayListExtra(TopTrackRetrievalService.TRACKS_EXTRA_KEY);
            mAdapter = new TrackListAdapter(getActivity(), SpotifyActivity.artistTracks);
            setListAdapter(mAdapter);
            if (SpotifyActivity.artistTracks.isEmpty()) {
                Toast.makeText(getActivity(), context.getString(R.string.no_results_found_please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }
    };


    public TrackActivityFragment() { }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY, (ArrayList<? extends Parcelable>)SpotifyActivity.artistTracks);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState && null != savedInstanceState.getParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY)) {
            SpotifyActivity.artistTracks = savedInstanceState.getParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY);
        }
        mAdapter = new TrackListAdapter(getActivity(), SpotifyActivity.artistTracks);
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(TopTrackRetrievalService.TOP_TRACK_BROADCAST_FILTER);
        getActivity().registerReceiver(topTracksBroadcastReceiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(topTracksBroadcastReceiver);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SpotifyActivity.launchMusicPlayerDialogFragment(((SpotifyActivity)getActivity()).getSupportFragmentManager(), position);
    }


}
