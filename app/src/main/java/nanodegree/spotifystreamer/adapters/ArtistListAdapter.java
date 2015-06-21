package nanodegree.spotifystreamer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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


    @Override
    public int getCount() {
        return spotifyArtists.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpotifyArtist spotifyArtist = spotifyArtists.get(position);
        if (null == convertView) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);

            TextView artistName = (TextView)convertView.findViewById(R.id.artistName);
            artistName.setText(spotifyArtist.getName());
        }
        return convertView;
    }


}
