package com.a6020peaks.bakingapp.ui.steps;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.a6020peaks.bakingapp.data.RecipeRepository;

/**
 * Created by narko on 28/09/17.
 */

public class StepDetailsFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private RecipeRepository mRecipeRepository;
    private int mStepId;

    public StepDetailsFragmentViewModelFactory(RecipeRepository repository, int stepId) {
        mRecipeRepository = repository;
        mStepId = stepId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new StepDetailsFragmentViewModel(mRecipeRepository, mStepId);
    }
}
