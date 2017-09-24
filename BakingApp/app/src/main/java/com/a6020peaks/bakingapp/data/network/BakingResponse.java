package com.a6020peaks.bakingapp.data.network;

import android.support.annotation.NonNull;

import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;

import java.util.List;

/**
 * Created by narko on 19/09/17.
 */

/**
 * Utility class to keep the data parsed from the JSON response
 */
public class BakingResponse {
    @NonNull
    private final List<RecipeEntry> mRecipes;

    @NonNull
    private final List<RecipeWithIngredients> mIngredients;

    private final List<RecipeWithSteps> mSteps;

    public BakingResponse(@NonNull List<RecipeEntry> recipes,
                          @NonNull List<RecipeWithIngredients> ingredients,
                          @NonNull List<RecipeWithSteps> steps) {
        mRecipes = recipes;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public List<RecipeEntry> getRecipes() {
        return mRecipes;
    }

    public List<RecipeWithIngredients> getIngredients() {
        return mIngredients;
    }

    public List<RecipeWithSteps> getSteps() {
        return mSteps;
    }
}
