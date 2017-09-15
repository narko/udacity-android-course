package com.a6020peaks.bakingapp.model;

/**
 * Created by narko on 15/09/17.
 */

public class Recipe {
    private String name;
    private String thumbnail;

    public Recipe(String name) {
        this.name = name;
        this.thumbnail = "http://lorempixel.com/400/200/food";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
