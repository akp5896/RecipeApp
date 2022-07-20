package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("ParseRecipeData")
public class ParseRecipeData extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_TIME = "time";
    public static final String KEY_SERVINGS = "servings";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_AUTHOR = "author";

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }
    public void setAuthor(String author) {
        put(KEY_TITLE, author);
    }
    public void setSummary(String summary) {
        put(KEY_SUMMARY, summary);
    }
    public void setTime(int time) {
        put(KEY_TIME, time);
    }
    public void setServings(int servings) {
        put(KEY_SERVINGS, servings);
    }
    public void setIngredients(List<String> ingredients) {
        put(KEY_INGREDIENTS, ingredients);
    }
    public void setSteps(List<Step> steps) {
        List<String> toLoad = new ArrayList<>();
        for(Step step : steps) {
            toLoad.add(step.getNumber() + ". " + step.getStep());
        }
        put(KEY_STEPS, toLoad);
    }

    public String getAuthor() {
        return getString(KEY_AUTHOR);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public String getSummary() {
        return getString(KEY_SUMMARY);
    }
    public int getTime() {
        return getInt(KEY_TIME);
    }
    public int getServings() {
        return getInt(KEY_SERVINGS);
    }
    public List<Ingredient> getIngredients() {
        List<Ingredient> result = new ArrayList<>();
        try {
        JSONArray jsonArray = getJSONArray(KEY_INGREDIENTS);
            for(int i = 0; i < jsonArray.length(); i++) {

                    result.add(new Ingredient(jsonArray.getString(i)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Step> getSteps() {
        List<Step> result = new ArrayList<>();
        try {
            JSONArray jsonArray = getJSONArray(KEY_STEPS);
            for(int i = 0; i < jsonArray.length(); i++) {

                result.add(new Step(i + 1, jsonArray.getString(i)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
