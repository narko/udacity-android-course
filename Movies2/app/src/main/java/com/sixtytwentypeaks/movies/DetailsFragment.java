package com.sixtytwentypeaks.movies;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sixtytwentypeaks.movies.data.MovieContract;
import com.sixtytwentypeaks.movies.model.Movie;
import com.sixtytwentypeaks.movies.model.Review;
import com.sixtytwentypeaks.movies.model.Trailer;
import com.sixtytwentypeaks.movies.utils.TMDBUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Movie>,
        TrailerAdapter.OnTrailerClickListener {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final int MOVIE_DETAILS_LOADER_ID = 0;
    private Context mContext;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        View layout = inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Movie movie = bundle.getParcelable(Intent.EXTRA_INTENT);
                TextView movieTitleTextView = (TextView) layout.findViewById(R.id.tv_movie_title);
                movieTitleTextView.setText(movie.getTitle());
                TextView movieDateTextView = (TextView) layout.findViewById(R.id.tv_movie_date);
                movieDateTextView.setText(movie.getReleaseDate());
                TextView movieRateTextView = (TextView) layout.findViewById(R.id.tv_movie_rate);
                movieRateTextView.setText(movie.getRating());
                TextView movieSynopsisTextView = (TextView) layout.findViewById(R.id.tv_movie_synopsis);
                movieSynopsisTextView.setText(movie.getSynopsis());
                ImageView moviePosterImageView = (ImageView) layout.findViewById(R.id.iv_poster);
                Picasso.with(getContext()).load(movie.getPosterURL().toString()).into(moviePosterImageView);
                mLoadingIndicator = (ProgressBar) layout.findViewById(R.id.pb_loading_indicator);

                // Set up trailers info
                mTrailerRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_movie_trailers);
                LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                mTrailerRecyclerView.setLayoutManager(trailerLinearLayoutManager);
                mTrailerRecyclerView.setNestedScrollingEnabled(false);
                mTrailerAdapter = new TrailerAdapter(this);
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

                // Set up reviews info
                mReviewRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_review_trailers);
                LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                mReviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
                mReviewRecyclerView.setNestedScrollingEnabled(false);
                mReviewAdapter = new ReviewAdapter();
                mReviewRecyclerView.setAdapter(mReviewAdapter);

                // Initialize loader
                getActivity().getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, bundle, this);

                // Set up favourite toggle button
                ToggleButton favouriteToggle = (ToggleButton) layout.findViewById(R.id.tb_favourite);
                // TODO set up ToggleButton taking into account if the movie is already saved in favourites
                favouriteToggle.setChecked(movie.getFavourite());
                favouriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { // Save movie
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
                            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                            values.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getReleaseDate());
                            values.put(MovieContract.MovieEntry.COLUMN_RATE, movie.getRating());
                            values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterURL().toString());
                            values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
                            Uri uri = getContext().getContentResolver()
                                    .insert(MovieContract.MovieEntry.CONTENT_URI, values);
                            if (uri != null) {
                                Log.d(TAG, uri.toString());
                            }
                        } else { // Delete movie
                            int id = movie.getId();
                            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                                    .appendEncodedPath(Integer.toString(id)).build();
                            Log.d(TAG, "Deleting: " + uri.toString());
                            getContext().getContentResolver().delete(uri, null, null);
                        }
                    }
                });
            }
        }

        return layout;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie>(getContext()) {
            Movie mMovie = null;

            @Override
            protected void onStartLoading() {
                if (mMovie != null && mMovie.getReviews() != null && mMovie.getTrailers() != null) {
                    deliverResult(mMovie);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Movie loadInBackground() {
                if (args != null) {
                    mMovie = args.getParcelable(Intent.EXTRA_INTENT);
                }
                try {
                    URL url = TMDBUtils.buildMovieTrailersUrl(mMovie.getMovieId());
                    String response = TMDBUtils.getResponseFromHttpUrl(url);
                    Log.d(TAG, response);
                    List<Trailer> trailers = TMDBUtils.buildTrailerList(response);
                    url = TMDBUtils.buildMovieReviewUrl(mMovie.getMovieId());
                    response = TMDBUtils.getResponseFromHttpUrl(url);
                    Log.d(TAG, response);
                    List<Review> reviews = TMDBUtils.buildReviewList(response);
                    mMovie.setTrailers(trailers);
                    mMovie.setReviews(reviews);

                    return mMovie;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Movie data) {
                mMovie = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mTrailerAdapter.setData(data.getTrailers());
        mReviewAdapter.setData(data.getReviews());
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        // Empty
    }

    @Override
    public void onClick(Trailer trailer) {
        Log.d(TAG, trailer.getYoutubeUrl().toString());
        Uri youtubeAppUri = Uri.parse("vnd.youtube:" + trailer.getKey());
        Intent youtubeAppIntent = new Intent(Intent.ACTION_VIEW, youtubeAppUri);
        try {
            startActivity(youtubeAppIntent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "Youtube app not available. Starting browser...");
            Intent youtubeBrowserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(trailer.getYoutubeUrl().toString()));
            startActivity(youtubeBrowserIntent);
        }
    }
}
