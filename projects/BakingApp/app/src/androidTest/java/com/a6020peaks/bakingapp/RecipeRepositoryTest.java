package com.a6020peaks.bakingapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by narko on 26/09/17.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeRepositoryTest {
    private RecipeRepository mRepository;
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeDependencies() {
        mRepository = InjectorUtils.provideRepository(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void getRecipeDetailsTest() throws InterruptedException {
        new AsyncTask<Void, Void, List<RecipeEntry>>() {

            @Override
            protected List<RecipeEntry> doInBackground(Void... voids) {
                List<RecipeEntry> recipes = null;
                try {
                    recipes = LiveDataTestUtil.getValue(mRepository.getRecipes());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return recipes;
            }

            @Override
            protected void onPostExecute(List<RecipeEntry> recipes) {
                assertTrue(recipes != null && recipes.size() > 0);
                RecipeEntry recipe = recipes.get(0);
                System.out.println(recipe.getId());
            }
        }.execute();
    }
}
