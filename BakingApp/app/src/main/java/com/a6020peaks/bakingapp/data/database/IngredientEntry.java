package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by narko on 18/09/17.
 */
@Entity(tableName = "ingredient")
public class IngredientEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int quantity;
    private String measure;
    private String description;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    @Ignore
    public IngredientEntry() {}

    public IngredientEntry(int id, int quantity, String measure, String description, int recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.description = description;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
