package com.example.recipeapp.Models;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Retrofit.RetrofitAutocomplete;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Ingredient extends RetrofitAutocomplete implements Parcelable{
    @SerializedName("name")
    public abstract String getName();
    @SerializedName("id")
    @Nullable
    public abstract Long getId();

    public static TypeAdapter<Ingredient> typeAdapter(Gson gson) {
        return new AutoValue_Ingredient.GsonTypeAdapter(gson);
    }

    public Ingredient create(String name, Long id) {
        return new AutoValue_Ingredient(name, id);
    }
}
