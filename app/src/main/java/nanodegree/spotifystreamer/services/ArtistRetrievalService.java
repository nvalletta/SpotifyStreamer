package nanodegree.spotifystreamer.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import nanodegree.spotifystreamer.models.SpotifyArtist;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ArtistRetrievalService extends IntentService {

    public final static String ARTISTS_EXTRA_KEY = "ARTISTS";
    public final static String ARTIST_BROADCAST_FILTER = "ARTIST_SEARCH_RESULTS";
    public final static String ARTIST_QUERY_INTENT_KEY = "QUERY";

    private SpotifyApi spotifyApi = new SpotifyApi();


    public ArtistRetrievalService() {
        super("");
    }


    public ArtistRetrievalService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        sendArtistRequest(intent.getStringExtra(ARTIST_QUERY_INTENT_KEY));
    }


    private void sendArtistRequest(String query) {
        SpotifyService spotifyService = spotifyApi.getService();
        spotifyService.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistPager, Response response) {
                notifyOfSuccess(artistPager);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyOfFailure(error);
            }
        });
    }


    private void notifyOfSuccess(ArtistsPager artistPager) {
        List<SpotifyArtist> spotifyArtists = new ArrayList<>();
        List<Artist> artistList = artistPager.artists.items;
        for (Artist artist : artistList) {
            SpotifyArtist spotifyArtist = new SpotifyArtist(artist);
            spotifyArtists.add(spotifyArtist);
        }

        Intent responseIntent = new Intent(ARTIST_BROADCAST_FILTER);
        responseIntent.putParcelableArrayListExtra(ARTISTS_EXTRA_KEY, (ArrayList<? extends Parcelable>)spotifyArtists);
        sendBroadcast(responseIntent);
    }


    private void notifyOfFailure(RetrofitError error) {
        //TODO: handle failure
    }


}
