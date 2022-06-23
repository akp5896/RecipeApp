package com.example.recipeapp.Models;

import com.google.gson.annotations.SerializedName;

public class RecipeTitle {
    @SerializedName("title")
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
