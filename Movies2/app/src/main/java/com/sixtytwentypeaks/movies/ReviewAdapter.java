package com.sixtytwentypeaks.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sixtytwentypeaks.movies.model.Review;

import java.util.List;

/**
 * Created by narko on 16/02/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> mData;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        Review review = mData.get(position);
        holder.mReviewTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review_text);
        }
    }

    public void setData(List<Review> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
