package nanodegree.spotifystreamer.fragments;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.adapters.TrackListAdapter;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public final class TrackActivityFragment extends ListFragment {


    private TrackListAdapter mAdapter;
    private BroadcastReceiver topTracksBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<SpotifyTrack> spotifyTracks = intent.getParcelableArrayListExtra(TopTrackRetrievalService.TRACKS_EXTRA_KEY);
            mAdapter = new TrackListAdapter(getActivity(), spotifyTracks);
            setListAdapter(mAdapter);
        }
    };


    public TrackActivityFragment() { }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<SpotifyTrack> spotifyTracks = new ArrayList<>();
        mAdapter = new TrackListAdapter(getActivity(), spotifyTracks);
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


}
