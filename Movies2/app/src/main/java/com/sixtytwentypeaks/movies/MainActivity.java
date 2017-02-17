package com.sixtytwentypeaks.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sixtytwentypeaks.movies.model.Movie;
import com.sixtytwentypeaks.movies.utils.TMDBUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TMDBAdapter.OnMovieClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_KEY = "urlKey";
    private static final int MOVIE_LOADER_ID = 1;
    private TMDBAdapter mTMDBAdapter;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mErrorTextView = (TextView) findViewById(R.id.tv_error);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTMDBAdapter = new TMDBAdapter(this);
        mRecyclerView.setAdapter(mTMDBAdapter);
        if (TMDBUtils.isDeviceOnline()) {
            hideErrorMessage();
            loadMovies(TMDBUtils.getPopularMoviesUrl());
        } else {
            displayErrorMessage();
        }
    }

    /**
     * Loads the movies from the given url into the UI
     * @param url TMDB url to fetch the movie list from
     */
    private void loadMovies(URL url) {
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url.toString());
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundle, this);
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
                if (TMDBUtils.isDeviceOnline()) {
                    hideErrorMessage();
                    loadMovies(TMDBUtils.getPopularMoviesUrl());
                } else {
                    displayErrorMessage();
                }
                break;
            case R.id.action_top_rated_movies:
                if (TMDBUtils.isDeviceOnline()) {
                    hideErrorMessage();
                    loadMovies(TMDBUtils.getTopRatedMoviesUrl());
                } else {
                    displayErrorMessage();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> mData = null;

            @Override
            protected void onStartLoading() {
                if (mData != null && !mData.isEmpty()) {
                    deliverResult(mData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    URL url;
                    if (args != null) {
                        String urlString = args.getString(URL_KEY);
                        url = new URL(urlString);
                    } else {
                        return null;
                    }
                    String response = TMDBUtils.getResponseFromHttpUrl(url);
                    return TMDBUtils.buildMovieList(response);
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
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Empty
    }
}
