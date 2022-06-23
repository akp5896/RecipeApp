package com.example.recipeapp.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Recipe {
    @SerializedName("title")
    String title;
    @SerializedName("image")
    String image;
    @SerializedName("id")
    Long id;
    @SerializedName("readyInMinutes")
    Integer readyInMinutes;
    @SerializedName("servings")
    Integer servings;
    List<String> analyzedInstructions;
    List<Ingredient> ingredients;

    public static Recipe fromJson(JSONObject object)
    {
        Recipe recipe = new Recipe();
        try {
            recipe.title = object.getString("title");
            recipe.image = object.getString("image");
            recipe.id = object.getLong("id");
            recipe.readyInMinutes = object.getInt("readyInMinutes");
            recipe.servings = object.getInt("servings");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public static void Steps(Recipe recipe, JSONObject json) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray steps = json.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");
            for(int i = 0; i < steps.length(); i++) {
                result.add(String.valueOf(i + 1) + ". " + steps.getJSONObject(i).getString("step"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipe.setAnalyzedInstructions(result);
    }

    public static void Ingredients(Recipe recipe, JSONObject json) {
        List<Ingredient> result = new ArrayList<>();
        try {
            JSONArray steps = json.getJSONArray("extendedIngredients");
            for(int i = 0; i < steps.length(); i++) {
                result.add(
                        new Ingredient(
                                steps.getJSONObject(i).getString("original"),
                                steps.getJSONObject(i).getLong("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipe.ingredients = result;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setAnalyzedInstructions(List<String> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public Long getId() {
        return id;
    }

    public Integer getTimeToCook() {
        return readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public List<String> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
