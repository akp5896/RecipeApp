package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.API.Step;
import com.parse.ParseClassName;
import com.parse.ParseObject;

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

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }
    public void setSummary(String title) {
        put(KEY_SUMMARY, title);
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
}
