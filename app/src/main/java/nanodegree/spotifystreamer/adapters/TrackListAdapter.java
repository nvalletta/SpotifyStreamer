package nanodegree.spotifystreamer.adapters;


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


    private final Context context;
    private final List<SpotifyTrack> tracks;


    public TrackListAdapter(Context context, List<SpotifyTrack> tracks) {
        super(context, R.layout.list_item);
        this.context = context;
        this.tracks = tracks;
    }


    @Override
    public int getCount() {
        return tracks.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SpotifyTrack track = tracks.get(position);
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.mainText = (TextView)convertView.findViewById(R.id.mainText);
            holder.secondaryText = (TextView)convertView.findViewById(R.id.secondaryText);
            holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        populateViewHolderData(track, holder);

        return convertView;
    }


    private void populateViewHolderData(SpotifyTrack track, ViewHolder holder) {
        holder.mainText.setText(track.getTrackName());
        if (null != track.getAlbumName() && !track.getAlbumName().isEmpty()) {
            holder.secondaryText.setText(track.getAlbumName());
            holder.secondaryText.setVisibility(View.VISIBLE);
        }
        if (null != track.getAlbumThumbnailUrl()) {
            Picasso.with(context).load(track.getAlbumThumbnailUrl()).into(holder.thumbnail);
        } else {
            holder.thumbnail.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
        }
    }


    private SpotifyTrack getTrackAtIndex(int index) {
        return this.tracks.get(index);
    }


    private class ViewHolder {
        TextView mainText;
        TextView secondaryText;
        ImageView thumbnail;
    }


}
