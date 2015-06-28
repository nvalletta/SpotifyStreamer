package nanodegree.spotifystreamer.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.models.SpotifyTrack;

public class TrackListAdapter extends ArrayAdapter {


    private Context context;
    private List<SpotifyTrack> tracks;


    public TrackListAdapter(Context context, List<SpotifyTrack> tracks) {
        super(context, R.layout.list_item);
        this.context = context;
        this.tracks = tracks;
    }


    private SpotifyTrack getTrackAdIndex(int index) {
        return this.tracks.get(index);
    }


    @Override
    public int getCount() {
        return tracks.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpotifyTrack track = tracks.get(position);
        if (null == convertView) {

        }
        return convertView;
    }
}
