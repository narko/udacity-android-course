package com.a6020peaks.bakingapp.data.database;

import java.util.List;

/**
 * Created by narko on 02/10/17.
 */

public class RecipeDetails {
    private List<IngredientEntry> ingredients;
    private List<StepEntry> steps;

    public List<IngredientEntry> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntry> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepEntry> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntry> steps) {
        this.steps = steps;
    }
}
