package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by narko on 18/09/17.
 */

@Entity(tableName = "recipe")
public class RecipeEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String image;
    private int servings;


}
