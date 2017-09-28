package com.a6020peaks.bakingapp.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

/**
 * Created by narko on 25/09/17.
 */

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnStepItemClickListener {
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    private static final String RECIPE_ID = "recipe_id";
    private RecyclerView mDetailsRv;
    private RecipeDetailsAdapter mAdapter;
    private RecipeDetailsFragmentViewModel mViewModel;

    private OnStepItemClickListener mOnStepItemClickListener;

    public interface OnStepItemClickListener {
        void onStepItemClick(StepEntry item);
    }

    public static RecipeDetailsFragment create(int recipeId) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnStepItemClickListener = (OnStepItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepItemClick");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!(getArguments() != null && getArguments().containsKey(RECIPE_ID))) {
            throw new RuntimeException("No recipe given");
        }
        int recipeId = getArguments().getInt(RECIPE_ID);

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mDetailsRv = rootView.findViewById(R.id.recipe_details_rv);
        mDetailsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecipeDetailsAdapter(this);
        mDetailsRv.setAdapter(mAdapter);

        RecipeDetailsFragmentViewModelFactory factory = InjectorUtils
                .provideRecipeDetailsFragmentViewModelFactory(getContext(), recipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailsFragmentViewModel.class);
//        mViewModel.getRecipeDetails().observe(this, recipeDetails -> {
//            mAdapter.swapData(recipeDetails);
//        });

        mViewModel.getIngredients().observe(this, ingredients -> {
            mAdapter.swapIngredients(ingredients);
        });

        mViewModel.getSteps().observe(this, steps -> {
            mAdapter.swapSteps(steps);
        });

        return rootView;
    }

    @Override
    public void onStepItemClick(StepEntry item) {
        mOnStepItemClickListener.onStepItemClick(item);
    }
}
