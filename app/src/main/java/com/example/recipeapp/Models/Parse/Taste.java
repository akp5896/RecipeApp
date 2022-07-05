package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Utils.Recommendation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ParseClassName("UserTaste")
public class Taste extends ParseObject {
    public static final List<String> COMPONENTS = new ArrayList<String>() {
        {
            add("sweetness");
            add("saltiness");
            add("sourness");
            add("bitterness");
            add("savoriness");
            add("fattiness");
            add("spiciness");
        }
    };

    private static final int NUMBER_OF_COMPONENTS = COMPONENTS.size();

    private HashMap<String, Double> componentValues = new HashMap<>();

    public void updateTaste(Taste newTaste, int numberOfVotes) {
        try {
            fetchIfNeeded();
            for(String component : COMPONENTS) {
                updateAverage(component, newTaste.getComponentValues().getOrDefault(component, 0.0), numberOfVotes);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NotNull
    public HashMap<String, Double> getComponentValues() {
        if(componentValues == null) {
            componentValues = new HashMap<>();
        }
        return componentValues;
    }

    public void setComponentValues(HashMap<String, Double> componentValues) {
        this.componentValues = componentValues;
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
        for(String component : COMPONENTS) {
            total += Recommendation.getNormalDistance(getDouble(component), otherTaste.componentValues.getOrDefault(component, 0.0)) / NUMBER_OF_COMPONENTS;
        }
        return total;
    }
}
