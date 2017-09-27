package com.a6020peaks.bakingapp.ui.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeDetails;
import com.a6020peaks.bakingapp.data.database.StepEntry;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by narko on 25/09/17.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<IngredientEntry> mIngredients;
    private static List<StepEntry> mSteps;
    private OnStepItemClickListener mOnStepItemClickListener;


    public interface OnStepItemClickListener {
        void onStepItemClick(StepEntry item);
    }

    public RecipeDetailsAdapter(OnStepItemClickListener listener) {
        this.mOnStepItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_view, parent, false);
                return new IngredientViewHolder(view);
            case 1:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_view_item, parent, false);
                return new StepViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ((IngredientViewHolder)holder).bind();
                break;
            case 1:
                ((StepViewHolder)holder).bind(mSteps.get(position), mOnStepItemClickListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSteps != null? mSteps.size() : 0; // There are always one item for ingredients
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void swapData(RecipeDetails recipeDetails) {
        mIngredients = recipeDetails.getIngredients();
        mSteps = recipeDetails.getSteps();
        notifyDataSetChanged();
    }

    public void swapIngredients(List<IngredientEntry> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public void swapSteps(List<StepEntry> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientTv;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTv = itemView.findViewById(R.id.ingredient);
        }

        public void bind() {
            if (mIngredients != null && mIngredients.size() > 0) {
                ingredientTv.setText(IngredientEntry.generateIngredientText(mIngredients));
            }
        }
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        private TextView descTv;

        public StepViewHolder(View itemView) {
            super(itemView);
            descTv = itemView.findViewById(R.id.step_desc);
        }

        public void bind(StepEntry item, OnStepItemClickListener listener) {
            descTv.setText(item.getShortDescription());
            itemView.setOnClickListener(view -> listener.onStepItemClick(item));
        }
    }
}
