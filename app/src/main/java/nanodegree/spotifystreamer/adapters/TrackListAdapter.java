package nanodegree.spotifystreamer.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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


    private SpotifyTrack getTrackAtIndex(int index) {
        return this.tracks.get(index);
    }


    private View instantiateConvertView(SpotifyTrack track) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View parentView = layoutInflater.inflate(R.layout.list_item, null);

        populateImageView(parentView, track);
        populateTextViews(parentView, track);

        return parentView;
    }


    private void populateImageView(View parent, SpotifyTrack track) {
        ImageView thumbnail = (ImageView)parent.findViewById(R.id.thumbnail);
        if (null != track.getAlbumThumbnailUrl()) {
            Picasso.with(context).load(track.getAlbumThumbnailUrl()).into(thumbnail);
        } else {
            thumbnail.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
        }
    }


    private void populateTextViews(View parent, SpotifyTrack track) {
        TextView mainText = (TextView)parent.findViewById(R.id.mainText);
        TextView secondaryText = (TextView)parent.findViewById(R.id.secondaryText);

        mainText.setText(track.getTrackName());
        if (null != track.getAlbumName() && !track.getAlbumName().isEmpty()) {
            secondaryText.setText(track.getAlbumName());
            secondaryText.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getCount() {
        return tracks.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpotifyTrack track = tracks.get(position);
        return instantiateConvertView(track);
    }
}
