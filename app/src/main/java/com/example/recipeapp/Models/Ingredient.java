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

@Parcel
public class Ingredient extends RetrofitAutocomplete {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    Long id;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
