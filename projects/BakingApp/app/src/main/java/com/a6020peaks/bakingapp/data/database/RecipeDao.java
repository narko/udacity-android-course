package com.a6020peaks.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(RecipeEntry... recipes);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    RecipeEntry getRecipe(int recipeId);

    @Query("SELECT * FROM recipe, step WHERE recipe.id = :recipeId AND step.recipe_id = recipe.id")
    RecipeWithSteps getRecipeWithSteps(int recipeId);

    @Query("SELECT * FROM recipe, ingredient WHERE recipe.id = :recipeId AND ingredient.recipe_id = recipe.id")
    RecipeWithIngredients getRecipeWithIngredients(int recipeId);

    @Query("SELECT * FROM recipe")
    LiveData<List<RecipeEntry>> getRecipes();

    @Query("DELETE FROM recipe")
    void deleteAll();
}
