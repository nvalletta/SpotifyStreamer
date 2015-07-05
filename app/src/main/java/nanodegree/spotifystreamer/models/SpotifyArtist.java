package nanodegree.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;


public class SpotifyArtist implements Parcelable {


    private final String name;
    private final String id;
    private String thumbnailUrl;


    public SpotifyArtist(Artist artist) {
        this.name = artist.name;
        this.id = artist.id;
        List<Image> images = artist.images;
        if (!images.isEmpty()) {
            this.thumbnailUrl = images.get(0).url;
        }
    }


    public String getName() {
        return this.name;
    }


    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }


    public String getId() {
        return this.id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.thumbnailUrl);
    }


    public static final Parcelable.Creator<SpotifyArtist> CREATOR = new Parcelable.Creator<SpotifyArtist>() {
        @Override
        public SpotifyArtist createFromParcel(Parcel source) {
            return new SpotifyArtist(source);
        }

        @Override
        public SpotifyArtist[] newArray(int size) {
            return new SpotifyArtist[size];
        }
    };


    private SpotifyArtist(Parcel parcel) {
        this.name = parcel.readString();
        this.id = parcel.readString();
        this.thumbnailUrl = parcel.readString();
    }


}
