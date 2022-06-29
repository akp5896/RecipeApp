package com.example.recipeapp.Models.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("dietCounter")
public class DietCounter extends ParseObject implements ParseCounter{
    public static String getParseClassName() {
        return "dietCounter";
    }

    public int getCount() {
        return getInt(Preferences.KEY_COUNTER);
    }

    public String getName() {
        return getString(Preferences.KEY_NAME);
    }
}
