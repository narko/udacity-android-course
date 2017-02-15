package com.sixtytwentypeaks.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movie> {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final int MOVIE_DETAILS_LOADER_ID = 0;
    private ProgressBar mLoadingIndicator;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Movie movie = bundle.getParcelable(Intent.EXTRA_INTENT);
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
                // Initialize loader
                getActivity().getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, bundle, this);
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
                    URL url = TMDBUtils.buildMovieTrailersUrl(mMovie.getId());
                    String response = TMDBUtils.getResponseFromHttpUrl(url);
                    Log.d(TAG, response);
                    List<Trailer> trailers = TMDBUtils.buildTrailerList(response);
                    url = TMDBUtils.buildMovieReviewUrl(mMovie.getId());
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
        // TODO show data in UI
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        // Empty
    }
}
