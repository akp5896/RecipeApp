package com.example.recipeapp.Models.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("cuisineCounter")
public class cuisineCounter extends ParseObject implements ParseCounter {
    public int getCount() {
        return getInt(Preferences.KEY_DIET_COUNTER);
    }

    public String getName() {
        return getString(Preferences.KEY_DIET_NAME);
    }
}
