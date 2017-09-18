package com.a6020peaks.bakingapp.model;

import java.util.List;

/**
 * Created by narko on 15/09/17.
 */

public class Recipe {
    private long id;
    private String name;
    private String image;
    private int servings;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Recipe(String name) {
        this.name = name;
        this.image = "http://lorempixel.com/400/200/food";
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
}
