package com.example.recipeapp.Models.API;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchApiCallParams {
    @Nullable
    public abstract String getTitle();
    @Nullable
    public abstract String getCuisine();
    @Nullable
    public abstract String getExcludeCuisine();
    @Nullable
    public abstract String getIncluded();
    @Nullable
    public abstract String getExcluded();
    @Nullable
    public abstract String getType();
    @Nullable
    public abstract String getTime();

    public static SearchApiCallParams create(String title, String cuisine, String excludeCuisine, String included, String excluded, String type, String time) {
        return new AutoValue_SearchApiCallParams(title, cuisine, excludeCuisine, included, excluded, type, time);
    }

}
