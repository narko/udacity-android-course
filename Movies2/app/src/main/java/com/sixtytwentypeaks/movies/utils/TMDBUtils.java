package com.sixtytwentypeaks.movies.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.sixtytwentypeaks.movies.BuildConfig;
import com.sixtytwentypeaks.movies.data.MovieContract;
import com.sixtytwentypeaks.movies.model.Movie;
import com.sixtytwentypeaks.movies.model.Review;
import com.sixtytwentypeaks.movies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by narko on 25/01/17.
 */

public class TMDBUtils {
    /**
     * Personal user API key.
     *
     * In order to use the key that you obtained from themoviedb.org
     * please follow this process:
     *
     *  1. Open [USER_HOME]/.gradle/gradle.properties. Create the file gradle.properties if
     *      it doesn't exist yet.
     *  2. Add the following line: MyTheMovieDBApiToken="XXXXX". Replace "XXXXX" with your key.
     */
    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    /****************************
     * Movie lists URL constants
     ****************************/

    public final static String BASE_URL = "https://api.themoviedb.org/3";

    /**
     * Get the top rated movies on TMDb.
     */
    public final static String TOP_RATED_PATH = "movie/top_rated";

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily.
     */
    public final static String POPULAR_PATH = "movie/popular";

    public final static String PARAM_API_KEY = "api_key";

    public final static String PARAM_LANGUAGE = "language";

    public final static String DEFAULT_LANGUAGE = "en-US";

    public final static String PARAM_PAGE = "page";

    public final static String ID_TEMPLATE = "{id}";

    /**
     * List of videos associated to a movie with given id = {id}
     */
    public final static String TRAILERS_TEMPLATE_PATH = "movie/" + ID_TEMPLATE + "/videos";

    /**
     * List of user reviews associated to a movie with given id = {id}
     */
    public final static String REVIEWS_TEMPLATE_PATH = "movie/" + ID_TEMPLATE + "/reviews";


    /****************************
     * Poster path URL constants
     ****************************/

    public final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    /**
     * The expected values for the poster size are:
     * "w92", "w154", "w185", "w342", "w500", "w780", or "original"
     */
    public final static String DEFAULT_POSTER_SIZE = "w185";


    /****************************
     * JSON strings
     ****************************/
    private final static String RESULT = "results";


    public static String getApiKey() {
        return API_KEY;
    }

    public static URL getPopularMoviesUrl() {
        return buildMovieListUrl(POPULAR_PATH);
    }

    public static URL getTopRatedMoviesUrl() {
        return buildMovieListUrl(TOP_RATED_PATH);
    }

    public static List<URL> buildPosterPathList(String response) {
        List<URL> posterPaths = new ArrayList<URL>();
        try {
            JSONObject jsonData = new JSONObject(response);
            JSONArray movies = jsonData.getJSONArray(RESULT);
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                String path = movie.getString("poster_path");
                // the poster path contains a '/'. We need to remove it in order to build our URL
                path = path.substring(1);
                posterPaths.add(buildPosterPathUrl(path));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posterPaths;
    }

    /**
     * Given a JSON response, this method parses the data and returns the result
     * as a list of Movie objects
     * @param response
     * @return
     */
    public static List<Movie> buildMovieList(String response) {
        List<Movie> movieList = new ArrayList<Movie>();
        try {
            if (response != null && !response.isEmpty()) {
                JSONObject jsonData = new JSONObject(response);
                //TODO control that there is the expected JSON and not an error
                JSONArray jsonMovies = jsonData.getJSONArray(RESULT);
                for (int i = 0; i < jsonMovies.length(); i++) {
                    JSONObject jsonMovie = jsonMovies.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setMovieId(jsonMovie.getString(Movie.MOVIE_ID));
                    movie.setTitle(jsonMovie.getString(Movie.TITLE));
                    movie.setSynopsis(jsonMovie.getString(Movie.SYNOPSIS));
                    movie.setReleaseDate(jsonMovie.getString(Movie.DATE));
                    movie.setRating(jsonMovie.getString(Movie.RATING));
                    movie.setPosterURL(buildPosterPathUrl(jsonMovie.getString(Movie.POSTER_PATH)));
                    movieList.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    /**
     * Given a JSON response, this method parses the data and returns the result
     * as a list of Review objects
     * @param response
     * @return
     */
    public static List<Review> buildReviewList(String response) {
        List<Review> reviewList = new ArrayList<>();
        try {
            if (response != null && !response.isEmpty()) {
                JSONObject jsonData = new JSONObject(response);
                JSONArray jsonReviews = jsonData.getJSONArray(RESULT);
                for (int i = 0; i < jsonReviews.length(); i++) {
                    JSONObject jsonReview = jsonReviews.getJSONObject(i);
                    Review review = new Review();
                    review.setId(jsonReview.getString(Review.ID));
                    review.setAuthor(jsonReview.getString(Review.AUTHOR));
                    review.setContent(jsonReview.getString(Review.CONTENT));
                    reviewList.add(review);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    /**
     * Given a JSON response, this method parses the data and returns the result
     * as a list of Trailer objects
     * @param response
     * @return
     */
    public static List<Trailer> buildTrailerList(String response) {
        List<Trailer> trailerList = new ArrayList<>();
        try {
            if (response != null && !response.isEmpty()) {
                JSONObject jsonData = new JSONObject(response);
                JSONArray jsonTrailers = jsonData.getJSONArray(RESULT);
                for (int i = 0; i < jsonTrailers.length(); i++) {
                    JSONObject jsonTrailer = jsonTrailers.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.setId(jsonTrailer.getString(Trailer.ID));
                    trailer.setKey(jsonTrailer.getString(Trailer.KEY));
                    trailer.setName(jsonTrailer.getString(Trailer.NAME));
                    trailer.setSite(jsonTrailer.getString(Trailer.SITE));
                    trailer.setSize(jsonTrailer.getString(Trailer.SIZE));
                    trailer.setType(jsonTrailer.getString(Trailer.TYPE));
                    trailerList.add(trailer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailerList;
    }

    /**
     * Builds the URL needed to get data from themoviedb.org
     * NOTE: For the initial version we will use the default en-US language
     * and we will not handle pagination
     *
     * @param path The path used to retrieve the list of movies, i.e., movies/popular
     * @return The URL to talk to the server
     */
    // TODO (1) handle api language
    // TODO (2) handle api pagination of results
    public static URL buildMovieListUrl(String path) {
        Uri uri = Uri.parse(TMDBUtils.BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(TMDBUtils.PARAM_API_KEY, TMDBUtils.getApiKey())
                .appendQueryParameter(TMDBUtils.PARAM_LANGUAGE, TMDBUtils.DEFAULT_LANGUAGE)
                .appendQueryParameter(TMDBUtils.PARAM_PAGE, String.valueOf(1))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL to retrieve the videos associated to a given movie
     * @param movieId
     * @return
     */
    public static URL buildMovieTrailersUrl(String movieId) {
        String path = TRAILERS_TEMPLATE_PATH.replace(ID_TEMPLATE, movieId);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(TMDBUtils.PARAM_API_KEY, TMDBUtils.getApiKey())
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Builds the URL to retrieve the user reviews associated to a given movie
     * @param movieId
     * @return
     */
    public static URL buildMovieReviewUrl(String movieId) {
        String path = REVIEWS_TEMPLATE_PATH.replace(ID_TEMPLATE, movieId);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(TMDBUtils.PARAM_API_KEY, TMDBUtils.getApiKey())
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String httpContent = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();
            //Trick to convert InputStream to String
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                httpContent = scanner.next();
            }
        } finally {
            urlConnection.disconnect();
        }
        return httpContent;
    }

    /**
     * Checks whether the device has access to the Internet. Solution taken from:
     * http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     * @return true if the device has online access.
     */
    public static boolean isDeviceOnline(Context context) {
        // This code seems to be stealing the thread and causing the main UI thread to block
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;

        // We use the recommended code pointed out by google in the docs:
        // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineType
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Transform a cursor containing Movie data to a List<Movie>.
     * This is a convenient method to reuse the existing TMDBAdapter.
     * @param cursor
     * @return List<Movie>
     */
    public static List<Movie> transformToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            movie.setMovieId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)));
            movie.setRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATE)));
            movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)));
            try {
                movie.setPosterURL(new URL(cursor.getString(cursor.getColumnIndex(
                        MovieContract.MovieEntry.COLUMN_POSTER))));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // If the movie is in the Cursor then it has been saved as favourite once
            movie.setFavourite(true);
            movies.add(movie);
        }
        return movies;
    }

    //****************************

    private static URL buildPosterPathUrl(String path) {
        URL url = null;
        Uri uri = Uri.parse(TMDBUtils.POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(TMDBUtils.DEFAULT_POSTER_SIZE)
                .appendEncodedPath(path)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
