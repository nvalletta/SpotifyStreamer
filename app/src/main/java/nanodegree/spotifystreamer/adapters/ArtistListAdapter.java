package nanodegree.spotifystreamer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

    private Context context;
    private List<SpotifyArtist> spotifyArtists;


    public ArtistListAdapter(Context context, List<SpotifyArtist> spotifyArtists) {
        super(context, R.layout.list_item);
        this.context = context;
        this.spotifyArtists = spotifyArtists;
    }


    private View instantiateConvertView(SpotifyArtist artist) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item, null);

        populateImageView(convertView, artist);
        populateArtistName(convertView, artist);

        return convertView;
    }


    private void populateImageView(View parent, SpotifyArtist artist) {
        ImageView artistThumbnailImageView = (ImageView)parent.findViewById(R.id.thumbnail);
        if (null != artist.getThumbnailUrl()) {
            Picasso.with(context).load(artist.getThumbnailUrl()).into(artistThumbnailImageView);
        } else {
            artistThumbnailImageView.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
        }
    }


    private void populateArtistName(View parent, SpotifyArtist artist) {
        TextView artistName = (TextView)parent.findViewById(R.id.artistName);
        artistName.setText(artist.getName());
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
        SpotifyArtist spotifyArtist = spotifyArtists.get(position);
        if (null == convertView) {
            convertView = instantiateConvertView(spotifyArtist);
        }
        return convertView;
    }


}
