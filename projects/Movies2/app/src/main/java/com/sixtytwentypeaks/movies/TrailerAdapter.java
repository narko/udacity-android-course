package com.sixtytwentypeaks.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sixtytwentypeaks.movies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by narko on 16/02/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private Context mContext;
    private List<Trailer> mData;
    private OnTrailerClickListener mOnTrailerClickListener;

    public interface OnTrailerClickListener {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(OnTrailerClickListener listener) {
        mOnTrailerClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        Trailer trailer = mData.get(position);
        URL thumbnailUrl = trailer.getThumbnailUrl();
        Picasso.with(mContext).load(thumbnailUrl.toString()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnTrailerClickListener.onClick(mData.get(position));
        }
    }

    public void setData(List<Trailer> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
