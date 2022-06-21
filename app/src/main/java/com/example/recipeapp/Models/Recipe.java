package com.example.recipeapp.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String title;
    private String image;
    private Long id;
    private Integer timeToCook;
    private Integer servings;
    private List<String> analyzedInstructions;

    public static Recipe fromJson(JSONObject object)
    {
        Recipe recipe = new Recipe();
        try {
            recipe.title = object.getString("title");
            recipe.image = object.getString("image");
            recipe.id = object.getLong("id");
            recipe.timeToCook = object.getInt("readyInMinutes");
            recipe.servings = object.getInt("servings");
            Steps(recipe, object.getJSONArray("analyzedInstructions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private static void Steps(Recipe recipe, JSONArray json) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray steps = json.getJSONObject(0).getJSONArray("steps");
            for(int i = 0; i < steps.length(); i++) {
                result.add(steps.getJSONObject(i).getString("step"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipe.setAnalyzedInstructions(result);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setTimeToCook(Integer timeToCook) {
        this.timeToCook = timeToCook;
    }

    public void setAnalyzedInstructions(List<String> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public Long getId() {
        return id;
    }

    public Integer getTimeToCook() {
        return timeToCook;
    }

    public List<String> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }
}
