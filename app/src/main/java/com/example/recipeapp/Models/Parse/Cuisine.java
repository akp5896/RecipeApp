package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("Cuisine")
public class Cuisine extends ParseObject {
    public void updateCuisine(Recipe recipe) {
        for(String cuisine : recipe.getCuisines()) {
            try {
                fetchIfNeeded();
                cuisine = cuisine.replaceAll("\\s+","");
                int x = getInt(cuisine);
                put(cuisine, x + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
