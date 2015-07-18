package nanodegree.spotifystreamer.fragments;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.adapters.TrackListAdapter;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import nanodegree.spotifystreamer.services.TopTrackRetrievalService;


public final class TrackActivityFragment extends ListFragment {


    private static final String SPOTIFY_TRACKS_PARCEL_KEY = "SPOTIFY_TRACKS";
    private List<SpotifyTrack> spotifyTracks = new ArrayList<>();
    private TrackListAdapter mAdapter;
    private final BroadcastReceiver topTracksBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            spotifyTracks = intent.getParcelableArrayListExtra(TopTrackRetrievalService.TRACKS_EXTRA_KEY);
            mAdapter = new TrackListAdapter(getActivity(), spotifyTracks);
            setListAdapter(mAdapter);
        }
    };


    public TrackActivityFragment() { }


    public void populateListWithTracks(List<SpotifyTrack> tracks) {
        this.spotifyTracks = tracks;
        mAdapter = new TrackListAdapter(getActivity(), tracks);
        setListAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY, (ArrayList<? extends Parcelable>)spotifyTracks);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState && null != savedInstanceState.getParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY)) {
            spotifyTracks = savedInstanceState.getParcelableArrayList(SPOTIFY_TRACKS_PARCEL_KEY);
        } else {
            spotifyTracks = new ArrayList<>();
        }
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
