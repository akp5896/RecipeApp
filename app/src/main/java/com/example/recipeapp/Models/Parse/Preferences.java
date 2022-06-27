package com.example.recipeapp.Models.Parse;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Recipe;
import com.parse.FindCallback;
import com.parse.Parse;
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


    public static final String KEY_DIET_NAME = "name";
    public static final String KEY_DIET_COUNTER = "counter";

    private static Preferences generalPreferences;

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

    private void updateCuisine(Recipe recipe) {
        ParseRelation<ParseObject> diet = getRelation(KEY_USER_CUISINE);
        diet.getQuery().whereContainedIn(KEY_DIET_NAME, recipe.getCuisines()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> copy = new ArrayList<>(recipe.getCuisines());
                for(ParseObject object : objects) {
                    copy.remove(object.getString(KEY_DIET_NAME));
                    object.increment(KEY_DIET_COUNTER);
                }
                List<cuisineCounter> newDiets = new ArrayList<>();
                for(String diet : copy) {
                    cuisineCounter dc = new cuisineCounter();
                    dc.put(KEY_DIET_NAME, diet);
                    dc.put(KEY_DIET_COUNTER, 1);
                    newDiets.add(dc);
                }
                ParseObject.saveAllInBackground(newDiets, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        for(cuisineCounter d : newDiets) {
                            diet.add(d);
                        }
                        saveInBackground();
                    }
                });
                ParseObject.saveAllInBackground(objects);
            }
        });
    }

    private void updateDiet(Recipe recipe) {
        ParseRelation<ParseObject> diet = getRelation(KEY_USER_DIET);
        diet.getQuery().whereContainedIn(KEY_DIET_NAME, recipe.getDiets()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> copy = new ArrayList<>(recipe.getDiets());
                for(ParseObject object : objects) {
                    copy.remove(object.getString(KEY_DIET_NAME));
                    object.increment(KEY_DIET_COUNTER);
                }
                List<dietCounter> newDiets = new ArrayList<>();
                for(String diet : copy) {
                    dietCounter dc = new dietCounter();
                    dc.put(KEY_DIET_NAME, diet);
                    dc.put(KEY_DIET_COUNTER, 1);
                    newDiets.add(dc);
                }
                ParseObject.saveAllInBackground(newDiets, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        for(dietCounter d : newDiets) {
                            diet.add(d);
                        }
                        saveInBackground();
                    }
                });
                ParseObject.saveAllInBackground(objects);
            }
        });
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
