package com.example.recipeapp.Models;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Retrofit.RetrofitAutocomplete;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Ingredient extends RetrofitAutocomplete {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    Long id;
    @SerializedName("title")
    Long title;

    public Ingredient(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
