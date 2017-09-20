package com.a6020peaks.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.a6020peaks.bakingapp.AppExecutors;
import com.a6020peaks.bakingapp.data.database.IngredientDao;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeDao;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;
import com.a6020peaks.bakingapp.data.database.StepDao;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.data.network.RecipeNetworkDataSource;
import com.a6020peaks.bakingapp.data.network.RecipeResponse;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */

public class RecipeRepository {
    private static final String TAG = RecipeRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;
    private final RecipeDao mRecipeDao;
    private final IngredientDao mIngredientDao;
    private final StepDao mStepDao;
    private final AppExecutors mExecutors;
    private final RecipeNetworkDataSource mNetworkDataSource;

    private RecipeRepository(RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao,
                             AppExecutors executors, RecipeNetworkDataSource networkDataSource) {
        mRecipeDao = recipeDao;
        mIngredientDao = ingredientDao;
        mStepDao = stepDao;
        mExecutors = executors;
        mNetworkDataSource = networkDataSource;
        LiveData<RecipeResponse> networkData = networkDataSource.getRecipeData();
        networkData.observeForever(new Observer<RecipeResponse>() {
            @Override
            public void onChanged(@Nullable final RecipeResponse recipeResponse) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize database
                        initializeDatabase(recipeResponse);
                        Log.d(TAG, "New values inserted");
                    }
                });
            }
        });
    }

    public synchronized static RecipeRepository getInstance(RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao,
                                                            AppExecutors executors, RecipeNetworkDataSource networkDataSource) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeRepository(recipeDao, ingredientDao, stepDao, executors, networkDataSource);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Room does not support relationships like other ORMs.
     * Current solution to intialize data in v1.0:
     * https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
     *
     * @param recipeResponse
     */
    private void initializeDatabase(RecipeResponse recipeResponse) {
        // Insert recipes without ingredients and steps
        mRecipeDao.bulkInsert(recipeResponse.getRecipes());

        // Insert ingredients for each recipe
        RecipeWithIngredients[] recipeWithIngredientsArray = recipeResponse.getIngredients();
        for (RecipeWithIngredients recWithIng : recipeWithIngredientsArray) {
            List<IngredientEntry> ingredients = recWithIng.getIngredients();
            for (IngredientEntry ingredient : ingredients) {
                ingredient.setRecipeId(recWithIng.getRecipe().getId());
            }
            mIngredientDao.bulkInsert(ingredients.toArray(new IngredientEntry[]{}));
        }

        // Insert steps for each recipe
        RecipeWithSteps[] recipeWithStepsArray = recipeResponse.getSteps();
        for (RecipeWithSteps recWithSteps: recipeWithStepsArray) {
            List<StepEntry> steps = recWithSteps.getSteps();
            for (StepEntry step : steps) {
                step.setRecipeId(recWithSteps.getRecipe().getId());
            }
            mStepDao.bulkInsert(steps.toArray(new StepEntry[]{}));
        }

    }
}
