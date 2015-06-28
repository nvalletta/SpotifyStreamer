package nanodegree.spotifystreamer.fragments;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import nanodegree.spotifystreamer.adapters.TrackListAdapter;
import nanodegree.spotifystreamer.models.SpotifyTrack;


public final class TrackActivityFragment extends ListFragment {


    private TrackListAdapter mAdapter;


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


}
