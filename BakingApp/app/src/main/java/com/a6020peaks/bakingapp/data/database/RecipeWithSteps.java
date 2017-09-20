package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */

public class RecipeWithSteps {
    @Embedded
    private RecipeEntry recipe;
    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = StepEntry.class)
    private List<StepEntry> steps;

    public RecipeEntry getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntry recipe) {
        this.recipe = recipe;
    }

    public List<StepEntry> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntry> steps) {
        this.steps = steps;
    }
}
