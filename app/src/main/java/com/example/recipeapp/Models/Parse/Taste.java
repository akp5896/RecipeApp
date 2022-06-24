package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UserTaste")
public class Taste extends ParseObject {
    public static final String KEY_SWEETNESS = "sweetness";
    public static final String KEY_SALTINESS = "saltiness";
    public static final String KEY_SOURNESS = "sourness";
    public static final String KEY_BITTERNESS = "bitterness";
    public static final String KEY_SAVORINESS = "savoriness";
    public static final String KEY_FATTINESS = "fattiness";
    public static final String KEY_SPICINESS = "spiciness";

    public void updateTaste(Recipe recipe, int numberOfVotes) {

    }

    private void updateAverage(String key, float newVal, int n) {
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }
}
