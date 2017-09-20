package com.a6020peaks.bakingapp.data.network;

import android.support.annotation.NonNull;

import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;

/**
 * Created by narko on 19/09/17.
 */

/**
 * Utility class to keep the data parsed from the JSON response
 */
public class RecipeResponse {
    @NonNull
    private final RecipeEntry[] mRecipes;

    @NonNull
    private final RecipeWithIngredients[] mIngredients;

    private final RecipeWithSteps[] mSteps;

    public RecipeResponse(@NonNull RecipeEntry[] recipes,
                          @NonNull RecipeWithIngredients[] ingredients,
                          @NonNull RecipeWithSteps[] steps) {
        mRecipes = recipes;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public RecipeEntry[] getRecipes() {
        return mRecipes;
    }

    public RecipeWithIngredients[] getIngredients() {
        return mIngredients;
    }

    public RecipeWithSteps[] getSteps() {
        return mSteps;
    }
}
