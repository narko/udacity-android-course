package com.a6020peaks.bakingapp.ui.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.a6020peaks.bakingapp.data.RecipeRepository;

/**
 * Created by narko on 25/09/17.
 */

public class RecipeDetailsFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private RecipeRepository mRepository;
    private int mRecipeId;

    public RecipeDetailsFragmentViewModelFactory(RecipeRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipeDetailsFragmentViewModel(mRepository, mRecipeId);
    }
}
