package com.a6020peaks.bakingapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
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
        // TODO this test is incomplete. There are issues happening when returning RecipeDetails
        // since most of the data is empty.

        List<RecipeEntry> recipes = LiveDataTestUtil.getValue(mRepository.getRecipes());
        assertTrue(recipes != null && recipes.size() > 0);
        RecipeEntry recipe = recipes.get(0);
        System.out.println(recipe.getId());
//        RecipeDetails recipeDetails = LiveDataTestUtil.getValue(mRepository.getRecipeDetails(recipe.getId()));
//        assertTrue(recipeDetails != null);
//        List<IngredientEntry> ingredients = recipeDetails.getRecipeWithIngredients();
//        assertTrue(ingredients != null);
//        IngredientEntry ingredient = ingredients.get(0);
//        assertTrue(ingredient.getRecipeId() == recipe.getId());

        RecipeWithIngredients recipeWithIngredients = mRepository.getRecipeWithIngredients(recipe.getId());
        assertTrue(recipeWithIngredients != null);
        System.out.println(recipeWithIngredients.getRecipe().getName());
        List<IngredientEntry> ingredients = recipeWithIngredients.getIngredients();
        assertNotNull(ingredients);
        IngredientEntry ingredientEntry = ingredients.get(0);
        System.out.println(ingredientEntry.getDescription());
        assertEquals(recipe.getId(), ingredientEntry.getRecipeId());
    }
}
