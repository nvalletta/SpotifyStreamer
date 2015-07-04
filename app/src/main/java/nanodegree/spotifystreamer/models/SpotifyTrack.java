package nanodegree.spotifystreamer.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class SpotifyTrack implements Parcelable {

    private String name;
    private String thumbnailUrl;

    public SpotifyTrack(Track track) {
        this.name = track.name;
        List<Image> images = track.album.images;
        if (!images.isEmpty()) {
            this.thumbnailUrl = track.album.images.get(0).url;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.thumbnailUrl);
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
        this.name = parcel.readString();
        this.thumbnailUrl = parcel.readString();
    }

}
