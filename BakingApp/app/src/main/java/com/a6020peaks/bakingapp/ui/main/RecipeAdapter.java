package com.a6020peaks.bakingapp.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by narko on 15/09/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<RecipeEntry> mRecipes;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(RecipeEntry item);
    }

    public RecipeAdapter(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(mRecipes.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    public void swapData(List<RecipeEntry> newRecipes) {
        mRecipes = newRecipes;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private ImageView thumbnail;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title);
            thumbnail = itemView.findViewById(R.id.image);
        }

        public void bind(final RecipeEntry item, final OnItemClickListener listener) {
            titleTv.setText(item.getName());
            String imagePath = item.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                Picasso.with(itemView.getContext())
                        .load(imagePath)
                        .placeholder(R.drawable.default_placeholder)
                        .error(R.drawable.default_placeholder)
                        .into(thumbnail);
            } else {
                Picasso.with(itemView.getContext())
                        .load(R.drawable.default_placeholder)
                        .error(R.drawable.default_placeholder)
                        .into(thumbnail);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
