package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by narko on 18/09/17.
 */
@Dao
public interface IngredientDao {
    @Insert
    void bulkInsert(IngredientEntry... ingredient);

    @Query("DELETE FROM ingredient")
    void deleteAll();
}
