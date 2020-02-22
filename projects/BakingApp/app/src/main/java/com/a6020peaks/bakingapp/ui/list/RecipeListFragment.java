package com.a6020peaks.bakingapp.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.ui.details.DetailsActivity;
import com.a6020peaks.bakingapp.ui.widget.RecipeAppWidget;
import com.a6020peaks.bakingapp.ui.widget.RecipeIntentService;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

/**
 * Created by narko on 25/09/17.
 */

public class RecipeListFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private RecipeListFragmentViewModel mViewModel;

    public RecipeListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recipe_rv);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        if (getResources().getBoolean(R.bool.isTablet)) {
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        RecipeListFragmentViewModelFactory factory = InjectorUtils.provideRecipeListFragmentViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeListFragmentViewModel.class);
        mViewModel.getRecipes().observe(this, recipeEntries -> {
            mRecipeAdapter.swapData(recipeEntries);
        });

        return rootView;
    }

    @Override
    public void onItemClick(RecipeEntry item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putInt(RecipeAppWidget.RECIPE_ID, item.getId()).apply();
        RecipeIntentService.startActionGetIngredients(getContext(), item.getId());
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.RECIPE_ID, item.getId());
        startActivity(intent);
    }
}
