package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Created by narko on 18/09/17.
 */
@Dao
public interface IngredientDao {
    @Insert
    void bulkInsert(IngredientEntry... ingredient);
}
