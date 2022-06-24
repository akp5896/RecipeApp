package com.example.recipeapp.Models.Parse;

import android.util.Log;

import com.example.recipeapp.Models.Recipe;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("Diet")
public class Diet extends ParseObject {
    public void updateDiet(Recipe recipe) {
        for(String diet : recipe.getDiets()) {
            try {
                fetchIfNeeded();
                diet = diet.replaceAll("\\s+","");
                int x = getInt(diet);
                put(diet, x + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
