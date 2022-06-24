package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.google.gson.annotations.SerializedName;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("UserTaste")
public class Taste extends ParseObject {
    public static final String KEY_SWEETNESS = "sweetness";
    public static final String KEY_SALTINESS = "saltiness";
    public static final String KEY_SOURNESS = "sourness";
    public static final String KEY_BITTERNESS = "bitterness";
    public static final String KEY_SAVORINESS = "savoriness";
    public static final String KEY_FATTINESS = "fattiness";
    public static final String KEY_SPICINESS = "spiciness";



    @SerializedName("sweetness")
    Double sweetness;
    @SerializedName("saltiness")
    Double saltiness;
    @SerializedName("sourness")
    Double sourness;
    @SerializedName("bitterness")
    Double bitterness;
    @SerializedName("savoriness")
    Double savoriness;
    @SerializedName("fattiness")
    Double fattiness;
    @SerializedName("spiciness")
    Double spiciness;

    public void updateTaste(Taste newTaste, int numberOfVotes) {
        try {
            fetchIfNeeded();
            updateAverage(KEY_SWEETNESS, newTaste.getSweetness(), numberOfVotes);
            updateAverage(KEY_SALTINESS, newTaste.getSaltiness(), numberOfVotes);
            updateAverage(KEY_SOURNESS, newTaste.getSourness(), numberOfVotes);
            updateAverage(KEY_BITTERNESS, newTaste.getBitterness(), numberOfVotes);
            updateAverage(KEY_SAVORINESS, newTaste.getSavoriness(), numberOfVotes);
            updateAverage(KEY_FATTINESS, newTaste.getFattiness(), numberOfVotes);
            updateAverage(KEY_SPICINESS, newTaste.getSpiciness(), numberOfVotes);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Double getSweetness() {
        return sweetness;
    }

    public Double getSaltiness() {
        return saltiness;
    }

    public Double getSourness() {
        return sourness;
    }

    public Double getBitterness() {
        return bitterness;
    }

    public Double getSavoriness() {
        return savoriness;
    }

    public Double getFattiness() {
        return fattiness;
    }

    public Double getSpiciness() {
        return spiciness;
    }

    private void updateAverage(String key, double newVal, int n) {
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }
}
