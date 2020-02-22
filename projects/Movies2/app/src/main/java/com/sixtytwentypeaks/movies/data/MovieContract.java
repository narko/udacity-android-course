package com.sixtytwentypeaks.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by narko on 17/02/17.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.sixtytwentypeaks.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    private MovieContract() {}

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        // id as it is stored in the TMDB movie API. Note that this id is different than the
        // column id from BaseColumns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_POSTER = "poster";
    }
}
