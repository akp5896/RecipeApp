package com.example.recipeapp.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String title;
    private String image;

    public static Recipe fromJson(JSONObject object)
    {
        Recipe recipe = new Recipe();
        try {
            recipe.setTitle(object.getString("title"));
            recipe.setImage(object.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public static List<Recipe> fromJsonArray(JSONArray array) {
        List<Recipe> result = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                result.add(fromJson(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
