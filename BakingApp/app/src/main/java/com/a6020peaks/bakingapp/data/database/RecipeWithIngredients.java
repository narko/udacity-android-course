package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */

public class RecipeWithIngredients {
    @Embedded
    private RecipeEntry recipe;
    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = IngredientEntry.class)
    private List<IngredientEntry> ingredients;

    public RecipeWithIngredients() {}

    public RecipeWithIngredients(RecipeEntry recipe, List<IngredientEntry> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public RecipeEntry getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntry recipe) {
        this.recipe = recipe;
    }

    public List<IngredientEntry> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntry> ingredients) {
        this.ingredients = ingredients;
    }
}
