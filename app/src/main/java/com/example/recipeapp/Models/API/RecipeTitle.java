package com.example.recipeapp.Models.API;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Retrofit.RetrofitAutocomplete;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RecipeTitle extends RetrofitAutocomplete {
    @SerializedName("title")
    String title;

    @Override
    public String getName() {
        return title;
    }
}
