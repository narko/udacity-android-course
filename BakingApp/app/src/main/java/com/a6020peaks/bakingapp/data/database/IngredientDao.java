package com.a6020peaks.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */
@Dao
public interface IngredientDao {
    @Insert
    void bulkInsert(IngredientEntry... ingredient);

    @Query("DELETE FROM ingredient")
    void deleteAll();

    @Query("SELECT * FROM ingredient where recipe_id = :recipeId")
    LiveData<List<IngredientEntry>> getRecipeIngredients(int recipeId);
}
