package com.example.recipeapp.Models.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("cuisineCounter")
public class CuisineCounter extends ParseObject implements ParseCounter {
    public int getCount() {
        return getInt(Preferences.KEY_COUNTER);
    }

    public String getName() {
        return getString(Preferences.KEY_NAME);
    }
}
