package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.Models.Recipe;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("Preferences")
public class Preferences extends ParseObject {
    public static final String KEY_NUMBER_OF_VOTES = "numberOfVotes";
    public static final String KEY_AVG_TIME = "avgTime";
    public static final String KEY_STD_TIME = "stdTime";
    public static final String KEY_AVG_PRICE = "avgPrice";
    public static final String KEY_AVG_HEALTH = "avgHealth";

    public static final String KEY_USER_TASTE = "userTaste";
    public static final String KEY_USER_DIET = "diet";
    public static final String KEY_USER_CUISINE = "cuisine";

    public static final String PREFERENCES = "preferences";


    /**
     * Update preferences with the parameters of the new recipe
     * @param recipe The liked recipe
     */
    public void updatePreferences(Recipe recipe) {
        increment(KEY_NUMBER_OF_VOTES);
        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        updateStd((float)recipe.getTimeToCook());

        updateAverage(KEY_AVG_TIME, (double)recipe.getTimeToCook());
        updateAverage(KEY_AVG_PRICE, recipe.getPricePerServing());
        updateAverage(KEY_AVG_HEALTH, recipe.getHealthScore());
    }

    /**
     * Updating standard deviation using Welford's online algorithm
     * @param newVal New value to update std
     */
    private void updateStd(float newVal) {
        // Standard deviation indicates how spread out are the values
        // Here it shows, whether the likes recipes were all close to one point
        // (ex. [1,2,1,2,2,1], or spread (ex. 1,10,1,10,1,1, 10)
        // This is used in the recommendation search, where maxTime = avgTime + std
        int n = getInt(KEY_NUMBER_OF_VOTES);
        double sigma = getDouble(KEY_STD_TIME);
        double avg = getDouble(KEY_AVG_TIME);
        double newAvg = ((n - 1) * avg + newVal) / n;
        put(KEY_STD_TIME, ((n - 1)*sigma + (newVal - avg)*(newVal - newAvg))/n);
    }

    private void updateAverage(String key, Double newVal) {
        int n = getInt(KEY_NUMBER_OF_VOTES);
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }
}
