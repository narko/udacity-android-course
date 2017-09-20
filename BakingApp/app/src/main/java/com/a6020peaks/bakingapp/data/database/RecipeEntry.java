package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by narko on 18/09/17.
 */
@Entity(tableName = "recipe")
public class RecipeEntry {
    @PrimaryKey
    private int id;
    private String name;
    private String image;
    private int servings;

    public RecipeEntry(int id, String name, String image, int servings) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
