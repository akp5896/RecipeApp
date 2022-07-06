package com.example.recipeapp.Models.API;

import com.google.gson.annotations.SerializedName;

public class RecipeWidget<T> {
    @SerializedName("url")
    public T url;
}
