package nanodegree.spotifystreamer.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class SpotifyTrack implements Parcelable {

    private final String trackName;
    private final String albumName;
    private String albumThumbnailUrl;
    private final String previewUrl;


    public SpotifyTrack(Track track) {
        this.trackName = track.name;
        this.albumName = track.album.name;
        this.previewUrl = track.preview_url;
        List<Image> images = track.album.images;
        if (!images.isEmpty()) {
            this.albumThumbnailUrl = track.album.images.get(0).url;
        }
    }


    public String getTrackName() {
        return trackName;
    }


    public String getAlbumName() {
        return albumName;
    }


    public String getAlbumThumbnailUrl() {
        return albumThumbnailUrl;
    }


    public String getPreviewUri() {
        return previewUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackName);
        dest.writeString(this.albumName);
        dest.writeString(this.previewUrl);
        dest.writeString(this.albumThumbnailUrl);
    }


    public static final Parcelable.Creator<SpotifyTrack> CREATOR = new Parcelable.Creator<SpotifyTrack>() {
        @Override
        public SpotifyTrack createFromParcel(Parcel source) {
            return new SpotifyTrack(source);
        }

        @Override
        public SpotifyTrack[] newArray(int size) {
            return new SpotifyTrack[size];
        }
    };


    private SpotifyTrack(Parcel parcel) {
        this.trackName = parcel.readString();
        this.albumName = parcel.readString();
        this.previewUrl = parcel.readString();
        this.albumThumbnailUrl = parcel.readString();
    }


}
