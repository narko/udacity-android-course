package com.a6020peaks.bakingapp.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.a6020peaks.bakingapp.data.RecipeRepository;

/**
 * Created by narko on 24/09/17.
 */

public class RecipeListFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private RecipeRepository mRecipeRepository;

    public RecipeListFragmentViewModelFactory(RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipeListFragmentViewModel(mRecipeRepository);
    }
}
