package com.a6020peaks.bakingapp.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;

import java.util.List;

/**
 * Created by narko on 24/09/17.
 */

public class RecipeListFragmentViewModel extends ViewModel {
    private RecipeRepository mRepository;
    private LiveData<List<RecipeEntry>> mRecipeList;

    public RecipeListFragmentViewModel(RecipeRepository repository) {
        mRepository = repository;
        mRecipeList = repository.getRecipes();
    }

    public LiveData<List<RecipeEntry>> getRecipes() {
        return mRecipeList;
    }
}
