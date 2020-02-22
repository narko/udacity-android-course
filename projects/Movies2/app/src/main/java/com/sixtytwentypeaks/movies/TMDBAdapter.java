package com.sixtytwentypeaks.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sixtytwentypeaks.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by narko on 25/01/17.
 */

public class TMDBAdapter extends RecyclerView.Adapter<TMDBAdapter.MovieViewHolder> {
    private List<Movie> mMovieData;
    private Context mContext;
    private OnMovieClickListener mMovieClickListener;

    public interface OnMovieClickListener {
        void onClick(Movie movie);
    }

    public TMDBAdapter(OnMovieClickListener clickListener) {
        mMovieClickListener = clickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mMovieData == null || mMovieData.isEmpty()) {
            return;
        }
        Movie movie = mMovieData.get(position);
        URL posterUrl = movie.getPosterURL();
        Picasso.with(mContext).load(posterUrl.toString()).into(holder.mPosterImageView);
        //TODO check how to show image placeholder with Picasso
    }

    @Override
    public int getItemCount() {
        return mMovieData == null ? 0 : mMovieData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mMovieClickListener.onClick(mMovieData.get(position));
        }
    }

    public void setMovieData(List<Movie> data) {
        mMovieData = data;
        notifyDataSetChanged();
    }
}
