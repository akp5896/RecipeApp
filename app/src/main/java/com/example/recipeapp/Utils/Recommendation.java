package com.example.recipeapp.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;
import androidx.preference.Preference;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Parse.CuisineCounter;
import com.example.recipeapp.Models.Parse.ParseCounter;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.Settings;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Recommendation {

    public static final int NUMBER_OF_SORTING_COMPONENTS = 3;
    private static final String TAG = "RECOMMENDATION: ";
    private static final int RECIPES_REQUESTED = 3;
    private static Random random;

    public static void recommend(RecommendCallback callback) {
        Preferences currentPreferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.PREFERENCES);
        Preferences generalPreferences = Preferences.getGeneralPreferences();
        if(generalPreferences == null) {
            Log.e(TAG, "Cannot find general preferences");
            return;
        }

        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Envelope<List<Recipe>>> call = service.getSortedRecipes(BuildConfig.API_KEY,
                getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_CUISINE),
                getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_DIET),
                RecipeApi.RANDOM_ORDER,
                String.valueOf(currentPreferences.getMaxTime().intValue()),
                Settings.getIntolerances(),
                RECIPES_REQUESTED,
                RecipeApi.RECIPE_INFORMATION_VALUE);
        call.enqueue(getSortingCallback(currentPreferences, callback));
    }

    private static String getCuisine(Preferences currentPreferences, Preferences generalPreferences) {
        if(Settings.getCuisines() != null) {
            if(random == null) {
                random = new Random();
            }
            return Settings.getCuisines().get(random.nextInt());
        }
        return getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_CUISINE);
    }

    private static String getDiet(Preferences currentPreferences, Preferences generalPreferences){
        if(Settings.getDiet() != null) {
            return Settings.getDiet();
        }
        return getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_DIET);
    }

    @NonNull
    private static Callback<Envelope<List<Recipe>>> getSortingCallback(Preferences current, RecommendCallback callback) {
        return new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                List<Recipe> recipes = response.body().results;
                Executor.Builder builder = new Executor.Builder();
                for(Recipe recipe : recipes) {
                    builder = builder.add(() -> recipe.getTaste(current));
                }
                builder = builder.callback(() -> {
                    Collections.sort(recipes, Comparator.comparingDouble(Recipe::getUserRating));
                    callback.OnRecommendationReturned(recipes);
                });
                builder.build().execute();
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Log.e(TAG, "Failed " + t);
            }
        };
    }

    public static String getListRecommendation(Preferences current, Preferences general, String key) {
        try {
            current.fetchIfNeeded();
            general.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<ParseObject> generalList = general.getRelationByKey(key);
        Double total = (double)general.getVotesCount();
        HashMap<String, Double> probabilities = new HashMap<>();
        for(ParseObject parseCounter : generalList) {
            ParseCounter curCounter = (ParseCounter) parseCounter;
            probabilities.put(curCounter.getName(), (curCounter.getCount()) / total);
        }

        List<ParseObject> currentList = current.getRelationByKey(key);

        Double userTotal = (double)current.getVotesCount();

        RandomCollection<String> selector = new RandomCollection<>();

        for(ParseObject parseCounter : currentList) {
            ParseCounter curCounter = (ParseCounter) parseCounter;
            double currentProb = curCounter.getCount() / userTotal;
            selector.add(currentProb - probabilities.getOrDefault(curCounter.getName(), 0.0), curCounter.getName());
        }
        return selector.next();
    }

    public static Double getRecipeDistance(Recipe recipe, Taste taste, Preferences current) {
        double total = 0.0;
        total += getNormalDistance(current.getHealth(), recipe.getHealthScore()) / NUMBER_OF_SORTING_COMPONENTS;
        total += getNormalDistance(current.getPrice(), recipe.getPricePerServing()) / NUMBER_OF_SORTING_COMPONENTS;
        total += current.getTaste().calculateTasteDistance(taste) / NUMBER_OF_SORTING_COMPONENTS;
        return total;
    }

    public static double getNormalDistance(double primary, double secondary) {
        return Math.abs(primary - secondary) / primary;
    }
}
