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
import nanodegree.spotifystreamer.models.SpotifyArtist;


public class ArtistListAdapter extends ArrayAdapter {

    private final Context context;
    private final List<SpotifyArtist> spotifyArtists;


    public ArtistListAdapter(Context context, List<SpotifyArtist> spotifyArtists) {
        super(context, R.layout.list_item);
        this.context = context;
        this.spotifyArtists = spotifyArtists;
    }


    public SpotifyArtist getSpotifyArtistAtIndex(int index) {
        return this.spotifyArtists.get(index);
    }


    @Override
    public int getCount() {
        return spotifyArtists.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SpotifyArtist spotifyArtist = spotifyArtists.get(position);
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.artistName = (TextView)convertView.findViewById(R.id.mainText);
            holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        populateViewHolderData(spotifyArtist, holder);

        return convertView;
    }


    private void populateViewHolderData(SpotifyArtist spotifyArtist, ViewHolder holder) {
        holder.artistName.setText(spotifyArtist.getName());
        if (null != spotifyArtist.getThumbnailUrl()) {
            Picasso.with(context).load(spotifyArtist.getThumbnailUrl()).into(holder.thumbnail);
        } else {
            holder.thumbnail.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
        }
    }


    private class ViewHolder {
        TextView artistName;
        ImageView thumbnail;
    }

}
