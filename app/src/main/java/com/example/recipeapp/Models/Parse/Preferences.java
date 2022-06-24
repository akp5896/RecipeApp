package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Recipe;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    private static Preferences generalPreferences;

    public static Preferences getGeneralPreferences() {
        try {
            if (generalPreferences == null) {
                ParseQuery<Preferences> query = ParseQuery.getQuery(Preferences.class);
                query.whereEqualTo(KEY_OBJECT_ID, BuildConfig.GENERAL_ID);
                generalPreferences = query.find().get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return generalPreferences;
    }

    public void updatePreferences(Recipe recipe, Taste newTaste) {
        if(!this.getObjectId().equals(BuildConfig.GENERAL_ID)) {
            Preferences gp = getGeneralPreferences();
            gp.updatePreferences(recipe, newTaste);
            gp.saveInBackground();
        }

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
        Taste taste = ((Taste)getParseObject(KEY_USER_TASTE));
        taste.updateTaste(newTaste, getInt(KEY_NUMBER_OF_VOTES));
        taste.saveInBackground();
        Diet diet = ((Diet)getParseObject(KEY_USER_DIET));
        diet.updateDiet(recipe);
        diet.saveInBackground();
        Cuisine cuisine = ((Cuisine)getParseObject(KEY_USER_CUISINE));
        cuisine.updateCuisine(recipe);
        cuisine.saveInBackground();


    }

    private void updateStd(float newTime) {
        int n = getInt(KEY_NUMBER_OF_VOTES);
        double sigma = getDouble(KEY_STD_TIME);
        double avg = getDouble(KEY_AVG_TIME);
        double newAvg = ((n - 1) * avg + newTime) / n;
        put(KEY_STD_TIME, ((n - 1)*sigma + (newTime - avg)*(newTime - newAvg))/n);
    }

    private void updateAverage(String key, Double newVal) {
        int n = getInt(KEY_NUMBER_OF_VOTES);
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }
}
