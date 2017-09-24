package com.a6020peaks.bakingapp.data.network;

import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.database.RecipeWithIngredients;
import com.a6020peaks.bakingapp.data.database.RecipeWithSteps;
import com.a6020peaks.bakingapp.data.database.StepEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by narko on 20/09/17.
 */

public final class BakingJsonParser {
    private static final String ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";

    private static final String INGREDIENTS = "ingredients";
    private static final String DESCRIPTION = "ingredient";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";

    private static final String STEPS = "steps";
    private static final String STEP_SHORT_DESC = "shortDescription";
    private static final String STEP_DESC = "description";
    private static final String STEP_VIDEO = "videoURL";
    private static final String STEP_IMAGE = "thumbnailURL";

    public BakingResponse parse(final String serverResponse) throws JSONException {
        if (serverResponse == null) return null;

        // TODO make sure the response contains JSON data and no other HTTP error

        JSONArray jsonRecipes = new JSONArray(serverResponse);
        List<RecipeEntry> recipes = new ArrayList<>(jsonRecipes.length());
        List<RecipeWithIngredients> recipeWithIngredientsList = new ArrayList<>(jsonRecipes.length());
        List<RecipeWithSteps> recipeWithStepsList = new ArrayList<>(jsonRecipes.length());

        for (int i = 0; i < jsonRecipes.length(); i++) {
            // Parse recipe
            JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);
            int id = jsonRecipe.getInt(ID);
            String name = jsonRecipe.getString(RECIPE_NAME);
            int servings = jsonRecipe.getInt(SERVINGS);
            String recipeImage = jsonRecipe.getString(RECIPE_IMAGE);
            RecipeEntry recipe = new RecipeEntry(id, name, recipeImage, servings);

            // Parse ingredients
            JSONArray jsonIngredients = jsonRecipe.getJSONArray(INGREDIENTS);
            List<IngredientEntry> ingredients = new ArrayList<>(jsonIngredients.length());
            for (int j = 0; j < jsonIngredients.length(); j++) {
                JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);
                IngredientEntry ingredient = new IngredientEntry();
                ingredient.setDescription(jsonIngredient.getString(DESCRIPTION));
                ingredient.setMeasure(jsonIngredient.getString(MEASURE));
                ingredient.setQuantity(jsonIngredient.getInt(QUANTITY));
                ingredient.setRecipeId(recipe.getId());
                ingredients.add(ingredient);
            }


            // Parse steps
            JSONArray jsonSteps = jsonRecipe.getJSONArray(STEPS);
            List<StepEntry> steps = new ArrayList<>(jsonSteps.length());
            for (int k = 0; k < jsonSteps.length(); k++) {
                JSONObject jsonStep = jsonSteps.getJSONObject(k);
                int stepId = jsonStep.getInt(ID);
                String shortDesc = jsonStep.getString(STEP_SHORT_DESC);
                String desc = jsonStep.getString(STEP_DESC);
                String video = jsonStep.getString(STEP_VIDEO);
                String image = jsonStep.getString(STEP_IMAGE);

                StepEntry step = new StepEntry(stepId, shortDesc, desc, video, image, recipe.getId());
                steps.add(step);
            }

            // save recipe, ingredients, steps
            recipes.add(recipe);
            RecipeWithIngredients recipeWithIngredients = new RecipeWithIngredients(recipe, ingredients);
            recipeWithIngredientsList.add(recipeWithIngredients);
            RecipeWithSteps recipeWithSteps = new RecipeWithSteps(recipe, steps);
            recipeWithStepsList.add(recipeWithSteps);
        }

        return new BakingResponse(recipes, recipeWithIngredientsList, recipeWithStepsList);
    }
}
