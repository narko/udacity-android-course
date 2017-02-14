package com.sixtytwentypeaks.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixtytwentypeaks.movies.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Movie movie = bundle.getParcelable(Intent.EXTRA_INTENT);
                TextView movieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
                movieTitleTextView.setText(movie.getTitle());
                TextView movieDateTextView = (TextView) findViewById(R.id.tv_movie_date);
                movieDateTextView.setText(movie.getReleaseDate());
                TextView movieRateTextView = (TextView) findViewById(R.id.tv_movie_rate);
                movieRateTextView.setText(movie.getRating());
                TextView movieSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
                movieSynopsisTextView.setText(movie.getSynopsis());
                ImageView moviePosterImageView = (ImageView) findViewById(R.id.iv_poster);
                Picasso.with(this).load(movie.getPosterURL().toString()).into(moviePosterImageView);
            }
        }
    }
}
