package com.a6020peaks.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.a6020peaks.bakingapp.AppExecutors;
import com.a6020peaks.bakingapp.data.database.IngredientDao;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeDao;
import com.a6020peaks.bakingapp.data.database.RecipeDetails;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;
import com.a6020peaks.bakingapp.data.database.StepDao;
import com.a6020peaks.bakingapp.data.database.StepEntry;
import com.a6020peaks.bakingapp.data.network.BakingNetworkDataSource;
import com.a6020peaks.bakingapp.data.network.BakingResponse;

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
    private final BakingNetworkDataSource mNetworkDataSource;

    private RecipeRepository(RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao,
                             AppExecutors executors, BakingNetworkDataSource networkDataSource) {
        mRecipeDao = recipeDao;
        mIngredientDao = ingredientDao;
        mStepDao = stepDao;
        mExecutors = executors;
        mNetworkDataSource = networkDataSource;
        LiveData<BakingResponse> networkData = networkDataSource.getRecipeData();
        networkData.observeForever(bakingResponse -> mExecutors.diskIO().execute(() -> {
            // Initialize database
            deleteDatabase();
            persistNetworkData(bakingResponse);
            Log.d(TAG, "New values inserted");
        }));
    }

    public synchronized static RecipeRepository getInstance(RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao,
                                                            AppExecutors executors, BakingNetworkDataSource networkDataSource) {
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
     * @param bakingResponse
     */
    private void persistNetworkData(BakingResponse bakingResponse) {
        // Insert recipes without ingredients and steps
        mRecipeDao.bulkInsert(bakingResponse.getRecipes().toArray(new RecipeEntry[]{}));

        // Insert ingredients for each recipe
        List<RecipeWithIngredients> recipeWithIngredientsArray = bakingResponse.getIngredients();
        for (RecipeWithIngredients recWithIng : recipeWithIngredientsArray) {
            List<IngredientEntry> ingredients = recWithIng.getIngredients();
            for (IngredientEntry ingredient : ingredients) {
                ingredient.setRecipeId(recWithIng.getRecipe().getId());
            }
            mIngredientDao.bulkInsert(ingredients.toArray(new IngredientEntry[]{}));
        }

        // Insert steps for each recipe
        List<RecipeWithSteps> recipeWithStepsArray = bakingResponse.getSteps();
        for (RecipeWithSteps recWithSteps: recipeWithStepsArray) {
            List<StepEntry> steps = recWithSteps.getSteps();
            for (StepEntry step : steps) {
                step.setRecipeId(recWithSteps.getRecipe().getId());
            }
            mStepDao.bulkInsert(steps.toArray(new StepEntry[]{}));
        }

    }

    private synchronized void initializeDatabase() {
        mExecutors.networkIO().execute(() -> {
            startFetchDataService();
        });
    }

    private void deleteDatabase() {
        mRecipeDao.deleteAll();
        mIngredientDao.deleteAll();
        mStepDao.deleteAll();
    }

    public void startFetchDataService() {
        mNetworkDataSource.startFetchDataService();
    }

    public LiveData<List<RecipeEntry>> getRecipes() {
        initializeDatabase();
        return mRecipeDao.getRecipes();
    }

    public RecipeWithIngredients getRecipeWithIngredients(int recipeId) {
        return mRecipeDao.getRecipeWithIngredients(recipeId);
    }

    public LiveData<List<IngredientEntry>> getIngredients(int recipeId) {
        return mIngredientDao.getRecipeIngredients(recipeId);
    }

    public LiveData<List<StepEntry>> getSteps(int recipeId) {
        return mStepDao.getRecipeSteps(recipeId);
    }

    public LiveData<StepEntry> getStep(int stepId) {
        return mStepDao.getStep(stepId);
    }

    public LiveData<RecipeDetails> getRecipeDetails(int recipeId) {
        MutableLiveData<RecipeDetails> detailsLiveData = new MutableLiveData<>();
        mExecutors.diskIO().execute(() -> {
            detailsLiveData.postValue(retrieveRecipeDetails(recipeId));
        });

        return detailsLiveData;
    }

    private RecipeDetails retrieveRecipeDetails(int recipeId) {
        final RecipeDetails details = new RecipeDetails();
        RecipeWithIngredients ingredients = mRecipeDao.getRecipeWithIngredients(recipeId);
        if (ingredients != null) {
            details.setIngredients(ingredients.getIngredients());
        }
        RecipeWithSteps steps = mRecipeDao.getRecipeWithSteps(recipeId);
        if (steps != null) {
            details.setSteps(steps.getSteps());
        }

        return details;
    }
}
