package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by narko on 18/09/17.
 */
@Dao
public interface StepDao {
    @Insert
    void bulkInsert(StepEntry... steps);

    @Query("DELETE FROM step")
    void deleteAll();
}
