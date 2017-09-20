package com.a6020peaks.bakingapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.a6020peaks.bakingapp.data.database.IngredientDao;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeDao;
import com.a6020peaks.bakingapp.data.database.RecipeDatabase;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;
import com.a6020peaks.bakingapp.data.database.StepDao;
import com.a6020peaks.bakingapp.data.database.StepEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class RecipeDatabaseTest {
    private RecipeDatabase mDb;
    private RecipeDao mRecipeDao;
    private IngredientDao mIngredientDao;
    private StepDao mStepDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
        mRecipeDao = mDb.recipeDao();
        mIngredientDao = mDb.ingredientDao();
        mStepDao = mDb.stepDao();
    }

    @After
    public void closeDb() throws Exception {
        mDb.close();
    }

    @Test
    public void recipeReadAndWriteTest() {
        RecipeEntry recipe = new RecipeEntry(1, "Biscuits", "", 2);
        mRecipeDao.bulkInsert(recipe);
        RecipeEntry fetchRecipe = mRecipeDao.getRecipe(1);
        assertThat(fetchRecipe.getId(), equalTo(recipe.getId()));

        IngredientEntry ingredient = new IngredientEntry(1, 250, "G", "Sugar", 1);
        mIngredientDao.bulkInsert(ingredient);
        RecipeWithIngredients recipeWithIngredients = mRecipeDao.getRecipeWithIngredients(1);
        assertTrue(recipeWithIngredients != null);
        fetchRecipe = recipeWithIngredients.getRecipe();
        assertThat(fetchRecipe.getId(), equalTo(recipe.getId()));
        List<IngredientEntry> ingredients = recipeWithIngredients.getIngredients();
        assertTrue(ingredients != null && ingredients.size() == 1);
        IngredientEntry fetchIngredient = ingredients.get(0);
        assertThat(ingredient.getId(), equalTo(fetchIngredient.getId()));

        StepEntry step = new StepEntry(1, "Cut potatoes", "Cut potatoes in daces", "", "", 1);
        mStepDao.bulkInsert(step);
        RecipeWithSteps recipeWithSteps = mRecipeDao.getRecipeWithSteps(1);
        assertTrue(recipeWithSteps != null);
        fetchRecipe = recipeWithSteps.getRecipe();
        assertThat(fetchRecipe.getId(), equalTo(recipe.getId()));
        List<StepEntry> steps = recipeWithSteps.getSteps();
        assertTrue(steps != null && steps.size() == 1);
        StepEntry fetchStep = steps.get(0);
        assertThat(step.getId(), equalTo(fetchStep.getId()));
    }
}
