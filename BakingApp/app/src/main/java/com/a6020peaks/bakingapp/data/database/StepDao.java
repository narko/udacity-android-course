package com.a6020peaks.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by narko on 18/09/17.
 */
@Dao
public interface StepDao {
    @Insert
    void bulkInsert(StepEntry... steps);

    @Query("DELETE FROM step")
    void deleteAll();

    @Query("SELECT * FROM step where recipe_id = :recipeId")
    LiveData<List<StepEntry>> getRecipeSteps(int recipeId);

    @Query("SELECT * FROM step where id = :stepId")
    LiveData<StepEntry> getStep(int stepId);
}
