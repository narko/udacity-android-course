package com.sixtytwentypeaks.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixtytwentypeaks.movies.model.Movie;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


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
            }
        }


        return layout;
    }

}
