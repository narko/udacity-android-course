package com.sixtytwentypeaks.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by narko on 14/02/17.
 */

public class Trailer implements Parcelable {

    private static final String YOUTUBE = "YouTube";
    /****************************
     * JSON strings
     ****************************/
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String KEY = "key";
    public static final String SITE = "site";
    public static final String SIZE = "size";
    public static final String TYPE = "type";

    private String id;
    private String name;
    private String key;
    private String site;
    private String size;
    private String type;


    public URL getThumbnailUrl() {
        URL url = null;
        try {
            if (YOUTUBE.equalsIgnoreCase(site.toLowerCase())) {
                url = new URL("http://img.youtube.com/vi/" + key + "/0.jpg");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public URL getYoutubeUrl() {
        URL url = null;
        try {
            if (YOUTUBE.equalsIgnoreCase(site.toLowerCase())) {
                url = new URL("https://www.youtube.com/watch?v=" + key);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.site);
        dest.writeString(this.size);
        dest.writeString(this.type);
    }

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.size = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
