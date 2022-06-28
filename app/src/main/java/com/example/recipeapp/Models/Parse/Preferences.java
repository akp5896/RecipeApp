package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Recipe;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

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

    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTER = "counter";

    private static Preferences generalPreferences;

    public static final String PREFERENCES = "preferences";

    public Taste getTaste() {
        return (Taste) getParseObject(KEY_USER_TASTE);
    }

    public Double getHealth() {
        return getDouble(KEY_AVG_HEALTH);
    }

    public Double getPrice() {
        return getDouble(KEY_AVG_PRICE);
    }

    public Integer getVotesCount() {
        return getInt(KEY_NUMBER_OF_VOTES);
    }

    public Double getMaxTime() {
        return getAverageTime() + getStd();
    }

    public Double getAverageTime() {
        return getDouble(KEY_AVG_TIME);
    }

    public Double getStd() {
        return Math.sqrt(getDouble(KEY_STD_TIME));
    }

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

    public List<ParseObject> getDietsList() {
        ParseQuery<ParseObject> query = getRelation(KEY_USER_DIET).getQuery();
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ParseObject> getRelationByKey(String key) {
        ParseQuery<ParseObject> query = getRelation(key).getQuery();
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the preferences of the current user based on a liked recipe. Also updates general preferences.
     * @param recipe Liked recipe
     * @param newTaste The Taste of liked recipe
     */
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


        updateDiet(recipe);

        updateCuisine(recipe);


    }

    /**
     * Increment number of occurrences of all cuisines the recipe fits by one. Create a CuisineCounter object if doesn't exist.
     * @param recipe Liked recipe.
     */
    private void updateCuisine(Recipe recipe) {
        ParseRelation<ParseObject> diet = getRelation(KEY_USER_CUISINE);
        diet.getQuery().whereContainedIn(KEY_NAME, recipe.getCuisines()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> copy = new ArrayList<>(recipe.getCuisines());
                for(ParseObject object : objects) {
                    copy.remove(object.getString(KEY_NAME));
                    object.increment(KEY_COUNTER);
                }
                List<CuisineCounter> newDiets = new ArrayList<>();
                for(String diet : copy) {
                    CuisineCounter dc = new CuisineCounter();
                    dc.put(KEY_NAME, diet);
                    dc.put(KEY_COUNTER, 1);
                    newDiets.add(dc);
                }
                ParseObject.saveAllInBackground(newDiets, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        for(CuisineCounter d : newDiets) {
                            diet.add(d);
                        }
                        saveInBackground();
                    }
                });
                ParseObject.saveAllInBackground(objects);
            }
        });
    }

    /**
     * Increment number of occurrences of all diets the recipe fits by one. Create a DietCounter object if doesn't exist.
     * @param recipe Liked recipe.
     */
    private void updateDiet(Recipe recipe) {
        ParseRelation<ParseObject> diet = getRelation(KEY_USER_DIET);
        diet.getQuery().whereContainedIn(KEY_NAME, recipe.getDiets()).findInBackground((objects, e) -> {
            List<String> copy = new ArrayList<>(recipe.getDiets());
            for(ParseObject object : objects) {
                copy.remove(object.getString(KEY_NAME));
                object.increment(KEY_COUNTER);
            }
            List<DietCounter> newDiets = new ArrayList<>();
            for(String newDiet : copy) {
                DietCounter dc = new DietCounter();
                dc.put(KEY_NAME, newDiet);
                dc.put(KEY_COUNTER, 1);
                newDiets.add(dc);
            }
            ParseObject.saveAllInBackground(newDiets, e1 -> {
                for(DietCounter newDiet : newDiets) {
                    diet.add(newDiet);
                }
                saveInBackground();
            });
            ParseObject.saveAllInBackground(objects);
        });
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

    /**
     * Updates the average values of the given key
     * @param key Corresponding value of this key will be updated.
     * @param newVal The value of the newly liked recipe
     */

    private void updateAverage(String key, Double newVal) {
        int n = getInt(KEY_NUMBER_OF_VOTES);
        double avg = getDouble(key);
        put(key, ((n - 1) * avg + newVal) / n);
    }
}
