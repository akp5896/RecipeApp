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

    public ArrayList<String> getNamesFromJsonArray(JsonHttpResponseHandler.JSON json) {
        ArrayList<String> suggestions = new ArrayList<>();
        try {
        JSONArray response = json.jsonArray;
        for (int i = 0; i < response.length(); i++) {
                suggestions.add(response.getJSONObject(i).getString("name"));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suggestions;
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
