package com.a6020peaks.bakingapp.utils;

import android.content.Context;

import com.a6020peaks.bakingapp.AppExecutors;
import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.RecipeDatabase;
import com.a6020peaks.bakingapp.data.network.BakingNetworkDataSource;
import com.a6020peaks.bakingapp.ui.details.RecipeDetailsFragmentViewModelFactory;
import com.a6020peaks.bakingapp.ui.list.RecipeListFragmentViewModelFactory;

/**
 * Created by narko on 24/09/17.
 */

//TODO replace with Dagger2
public class InjectorUtils {
    public static RecipeRepository provideRepository(Context context) {
        RecipeDatabase database = RecipeDatabase.getInstance(context);
        AppExecutors appExecutors = AppExecutors.getInstance();
        BakingNetworkDataSource networkDataSource = BakingNetworkDataSource.getInstance(context, appExecutors);
        return RecipeRepository.getInstance(database.recipeDao(), database.ingredientDao(), database.stepDao(),
                appExecutors, networkDataSource);
    }

    public static BakingNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context); // Needed by the sync services in order to have a repository instance
        AppExecutors appExecutors = AppExecutors.getInstance();
        return BakingNetworkDataSource.getInstance(context, appExecutors);
    }

    public static RecipeListFragmentViewModelFactory provideRecipeListFragmentViewModelFactory(Context context) {
        RecipeRepository repository = provideRepository(context);
        return new RecipeListFragmentViewModelFactory(repository);
    }

    public static RecipeDetailsFragmentViewModelFactory provideRecipeDetailsFragmentViewModelFactory(Context context, int recipeId) {
        RecipeRepository repository = provideRepository(context);
        return new RecipeDetailsFragmentViewModelFactory(repository, recipeId);
    }
}
