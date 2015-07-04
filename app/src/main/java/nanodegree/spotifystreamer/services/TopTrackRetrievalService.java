package nanodegree.spotifystreamer.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import nanodegree.spotifystreamer.models.SpotifyTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TopTrackRetrievalService extends IntentService {

    public final static String TRACKS_EXTRA_KEY = "SPOTIFY_TRACKS";
    public final static String TOP_TRACK_BROADCAST_FILTER = "TRACK_BROADCAST_FILTER";
    public final static String ARTIST_ID_INTENT_KEY = "ARTIST_ID";

    private SpotifyApi spotifyApi = new SpotifyApi();


    public TopTrackRetrievalService() {
        super("");
    }


    public TopTrackRetrievalService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        sendTopTrackRequestForArtist(intent.getStringExtra(ARTIST_ID_INTENT_KEY));
    }


    private void sendTopTrackRequestForArtist(String artistId) {
        SpotifyService spotifyService = spotifyApi.getService();
        spotifyService.getArtistTopTrack(artistId, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                notifyOfSuccess(tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyOfFailure(error);
            }
        });
    }


    private void notifyOfSuccess(Tracks tracks) {
        List<SpotifyTrack> spotifyTracks = new ArrayList<>();
        List<Track> trackList = tracks.tracks;
        for (Track track : trackList) {
            SpotifyTrack spotifyTrack = new SpotifyTrack(track);
            spotifyTracks.add(spotifyTrack);
        }

        Intent responseIntent = new Intent(TOP_TRACK_BROADCAST_FILTER);
        responseIntent.putParcelableArrayListExtra(TRACKS_EXTRA_KEY, (ArrayList<? extends Parcelable>)spotifyTracks);
        sendBroadcast(responseIntent);
    }


    private void notifyOfFailure(RetrofitError error) {
        //TODO: handle failure
    }


}
