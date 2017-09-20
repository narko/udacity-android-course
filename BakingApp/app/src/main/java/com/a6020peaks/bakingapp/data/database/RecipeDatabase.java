package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by narko on 19/09/17.
 */

@Database(entities = {RecipeEntry.class, IngredientEntry.class, StepEntry.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase{
    private static final String DATABASE_NAME = "baking";
    private static volatile RecipeDatabase sInstance;
    private static final Object LOCK = new Object();

    private RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract StepDao stepDao();
}
