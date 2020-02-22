package com.a6020peaks.bakingapp.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeDetails;
import com.a6020peaks.bakingapp.data.database.StepEntry;

import java.util.List;

/**
 * Created by narko on 25/09/17.
 */

public class RecipeDetailsFragmentViewModel extends ViewModel {
    private RecipeRepository mRepository;
    private LiveData<RecipeDetails> mRecipeDetails;
    private LiveData<List<IngredientEntry>> mIngredients;
    private LiveData<List<StepEntry>> mSteps;

    public RecipeDetailsFragmentViewModel(RecipeRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeDetails = mRepository.getRecipeDetails(recipeId);
        mIngredients = mRepository.getIngredients(recipeId);
        mSteps = mRepository.getSteps(recipeId);
    }

    public LiveData<RecipeDetails> getRecipeDetails() {
        return mRecipeDetails;
    }

    public LiveData<List<IngredientEntry>> getIngredients() { return mIngredients; }

    public LiveData<List<StepEntry>> getSteps() { return mSteps; }
}
