package com.example.recipeapp.Retrofit;

import com.google.gson.annotations.SerializedName;

public class Envelope<T> {
    @SerializedName("results")
    T results;
}
