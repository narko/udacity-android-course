package com.a6020peaks.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.a6020peaks.bakingapp.data.RecipeRepository;
import com.a6020peaks.bakingapp.data.database.IngredientEntry;
import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.utils.InjectorUtils;

public class RecipeIntentService extends IntentService {
    private static final String TAG = RecipeIntentService.class.getSimpleName();
    private static final String ACTION_GET_INGREDIENTS = "com.a6020peaks.bakingapp.action.get_ingredients";
    private static final String EXTRA_RECIPE_ID = "com.a6020peaks.bakingapp.ui.widget.extra.RECIPE_ID";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    public static void startActionGetIngredients(Context context, int recipeId) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_GET_INGREDIENTS);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_INGREDIENTS.equals(action)) {
                final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 1);
                handleActionGetIngredients(recipeId);
            }
        }
    }

    private void handleActionGetIngredients(int recipeId) {
        RecipeRepository repository = InjectorUtils.provideRepository(this);
        RecipeEntry recipe = repository.getRecipe(recipeId);
        Log.d(TAG, recipe.getName());
        repository.getIngredients(recipeId).observeForever(ingredients -> {
            String ingredientText = IngredientEntry.generateIngredientText(ingredients);
            Log.d(TAG, ingredientText);
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
            int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
            RecipeAppWidget.updateAppWidgets(this, widgetManager, widgetIds, recipeId,
                    recipe.getName(), ingredientText);
        });
    }
}
