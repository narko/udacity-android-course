package com.sixtytwentypeaks.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sixtytwentypeaks.movies.data.MovieContract;
import com.sixtytwentypeaks.movies.model.Movie;
import com.sixtytwentypeaks.movies.utils.TMDBUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TMDBAdapter.OnMovieClickListener,
        LoaderCallbacks<List<Movie>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_KEY = "urlKey";
    private static final int MOVIE_LOADER_ID = 1;

    // Preferences Key/Values
    private static final String SORTING_CRITERIA = "sorting_criteria";
    private static final String SORTING_TOP_RATED = "top_rated";
    private static final String SORTING_POPULAR = "popular";
    private static final String SORTING_FAVOURITE = "favourite";

    private TMDBAdapter mTMDBAdapter;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private TextView mNoFavouritesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mErrorTextView = (TextView) findViewById(R.id.tv_error);
        mNoFavouritesTextView = (TextView) findViewById(R.id.tv_empty_favourites);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTMDBAdapter = new TMDBAdapter(this);
        mRecyclerView.setAdapter(mTMDBAdapter);
        initializePreferences();
        String currentSorting = getCurrentSortingCriteria();
        if (currentSorting.equalsIgnoreCase(SORTING_POPULAR) || currentSorting.equalsIgnoreCase((SORTING_TOP_RATED))) {
            if (TMDBUtils.isDeviceOnline(this)) {
                hideConnectivityErrorMessage();
                loadMovies(TMDBUtils.getPopularMoviesUrl());
            } else {
                displayConnectivityErrorMessage();
            }
        } else if (currentSorting.equalsIgnoreCase(SORTING_FAVOURITE)) {
            loadMovies(null);
        }
    }

    /**
     * This method used SharedPreference to store the sorting criteria of the movies.
     * We use this method to initialize the key/value file.
     */
    private void initializePreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.contains(SORTING_CRITERIA)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SORTING_CRITERIA, SORTING_POPULAR);
            editor.apply();
        }
    }

    private void updateSharedPreferences(String sortingCriteria) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORTING_CRITERIA, sortingCriteria);
        editor.apply();
    }

    private String getCurrentSortingCriteria() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(SORTING_CRITERIA, SORTING_POPULAR);
    }

    /**
     * Loads the movies from the given url or exiting ContentProvider into the UI
     * @param url TMDB url to fetch the movie list from
     */
    private void loadMovies(URL url) {
        String sorting = getCurrentSortingCriteria();
        if (sorting.equalsIgnoreCase(SORTING_POPULAR) || sorting.equalsIgnoreCase(SORTING_TOP_RATED)) {
            Log.d(TAG, url.toString());
            Bundle bundle = new Bundle();
            bundle.putString(URL_KEY, url.toString());
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, bundle, this);
        } else if (sorting.equalsIgnoreCase(SORTING_FAVOURITE)) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_popular_movies:
                updateSharedPreferences(SORTING_POPULAR);
                if (TMDBUtils.isDeviceOnline(this)) {
                    hideConnectivityErrorMessage();
                    loadMovies(TMDBUtils.getPopularMoviesUrl());
                } else {
                    displayConnectivityErrorMessage();
                }
                break;
            case R.id.action_top_rated_movies:
                updateSharedPreferences(SORTING_TOP_RATED);
                if (TMDBUtils.isDeviceOnline(this)) {
                    hideConnectivityErrorMessage();
                    loadMovies(TMDBUtils.getTopRatedMoviesUrl());
                } else {
                    displayConnectivityErrorMessage();
                }
                break;
            case R.id.action_favourite_movies:
                updateSharedPreferences(SORTING_FAVOURITE);
                hideConnectivityErrorMessage();
                loadMovies(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayEmptyFavouritesMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoFavouritesTextView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyFavouritesMessage() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoFavouritesTextView.setVisibility(View.INVISIBLE);
    }

    private void displayConnectivityErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideConnectivityErrorMessage() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> mData = null;

            @Override
            protected void onStartLoading() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    URL url;
                    if (args != null) { // We load TOP_RATED or POPULAR movies
                        String urlString = args.getString(URL_KEY);
                        url = new URL(urlString);
                        String response = TMDBUtils.getResponseFromHttpUrl(url);
                        return TMDBUtils.buildMovieList(response);
                    } else { // We load FAVOURITE movies
                        Log.d(TAG, "Querying ContentProvider for favourite movies...");
                        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null, null, null, null, null);
                        List<Movie> movies = TMDBUtils.transformToList(cursor);
                        // we need to close the cursor because the system won't do it for us
                        cursor.close();
                        return movies;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mTMDBAdapter.setMovieData(data);
        //set RecyclerView scroller to initial position
        mRecyclerView.scrollToPosition(0);
        // show custom screen if there is no movie data available. We need this mostly for the favourite sorting
        if (data.isEmpty()) {
            displayEmptyFavouritesMessage();
        } else {
            hideEmptyFavouritesMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Empty
    }
}
