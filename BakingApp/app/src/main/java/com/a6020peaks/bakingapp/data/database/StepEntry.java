package com.a6020peaks.bakingapp.data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by narko on 18/09/17.
 */
@Entity(tableName = "step")
public class StepEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remoteId;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    @Ignore
    public StepEntry(int remoteId, String shortDescription, String description, String videoUrl, String thumbnailUrl, int recipeId) {
        this.recipeId = remoteId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
    }

    public StepEntry(int id, int remoteId, String shortDescription, String description, String videoUrl, String thumbnailUrl, int recipeId) {
        this.id = id;
        this.recipeId = remoteId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }
}
