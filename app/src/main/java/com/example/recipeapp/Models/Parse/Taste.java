package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Utils.Recommendation;
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

    // If a component is added/removed, this number should be updated
    private static final int NUMBER_OF_COMPONENTS = 7;

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

    /**
     * Updates the average values of the components of the taste
     * @param key Corresponding value of this key will be updated.
     * @param newVal The value of the newly liked recipe
     * @param n How many votes user alreay have
     */
    private void updateAverage(String key, Double newVal, int n) {
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }

    /**
     * Calculates how different the new taste from the average taste of the user
     * @param otherTaste Taste to compare
     * @return
     */
    public Double calculateTasteDistance(Taste otherTaste) {
        double total = 0.0;
        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        total += Recommendation.getNormalDistance(getDouble(KEY_SWEETNESS), otherTaste.sweetness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_FATTINESS), otherTaste.fattiness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_BITTERNESS), otherTaste.bitterness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_SAVORINESS), otherTaste.savoriness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_SOURNESS), otherTaste.sourness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_SALTINESS), otherTaste.saltiness) / NUMBER_OF_COMPONENTS;
        total += Recommendation.getNormalDistance(getDouble(KEY_SPICINESS), otherTaste.spiciness) / NUMBER_OF_COMPONENTS;
        return total;
    }
}
