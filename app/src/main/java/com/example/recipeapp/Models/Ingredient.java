package com.example.recipeapp.Models;

import android.os.Parcelable;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Retrofit.RetrofitAutocomplete;
import com.google.auto.value.AutoValue;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Ingredient extends RetrofitAutocomplete implements Parcelable {
    @SerializedName("name")
    public abstract String getName();
    @SerializedName("id")
    public abstract Long id();
    @SerializedName("title")
    public abstract Long title();

    public static Ingredient create(String name, Long id, Long title) {
        return new AutoValue_Ingredient(name, id, title);
    }


}
