package com.sixtytwentypeaks.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.List;

/**
 * Created by narko on 25/01/17.
 */

/**
 * The Parcelable related code has been implemented using the following plugin:
 * https://github.com/mcharmas/android-parcelable-intellij-plugin
 */
public class Movie implements Parcelable {
    /****************************
     * JSON strings
     ****************************/
    public final static String ID = "id";
    public final static String TITLE = "original_title";
    public final static String SYNOPSIS = "overview";
    public final static String RATING = "vote_average";
    public final static String DATE = "release_date";
    public final static String POSTER_PATH = "poster_path";

    private String title;
    private URL posterURL;
    private String synopsis;
    private String rating;
    private String releaseDate;
    private String id;
    private List<Review> reviews;
    private List<Trailer> trailers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public URL getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(URL posterURL) {
        this.posterURL = posterURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeSerializable(this.posterURL);
        dest.writeString(this.synopsis);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.id);
        dest.writeTypedList(this.reviews);
        dest.writeTypedList(this.trailers);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.posterURL = (URL) in.readSerializable();
        this.synopsis = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.id = in.readString();
        this.reviews = in.createTypedArrayList(Review.CREATOR);
        this.trailers = in.createTypedArrayList(Trailer.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
