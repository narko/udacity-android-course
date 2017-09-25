package com.a6020peaks.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a6020peaks.bakingapp.R;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_rv);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecipeListFragmentViewModelFactory factory = InjectorUtils.provideRecipeListFragmentViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeListFragmentViewModel.class);
        mViewModel.getRecipes().observe(this, recipeEntries -> {
            mRecipeAdapter.swapData(recipeEntries);
        });

        return rootView;
    }

    @Override
    public void onItemClick(RecipeEntry item) {

    }
}
