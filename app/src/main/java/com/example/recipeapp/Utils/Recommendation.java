package com.example.recipeapp.Utils;

import android.util.Log;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Parse.ParseCounter;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//        @Query("cuisine") String cuisine,
//        @Query("excludeCuisine") String excludeCuisine,
//        @Query("maxReadyTime") String maxReadyTime,
//        @Query("diet") String diet
public class Recommendation {
//    @GET("/recipes/complexSearch")
//    Call<Envelope<List<Recipe>>> getSortedRecipes(@Query("apiKey") String apiKey,
//                                                  @Query("cuisine") String cuisine,
//                                                  @Query("maxReadyTime") String maxReadyTime,
//                                                  @Query("addRecipeInformation") String addRecipeInformation,
//                                                  @Query("sort") String sortOrder,
//                                                  @Query("diet") String diet
//    );
    public static List<Recipe> recommend() {
        Preferences currentPreferences = (Preferences) ParseUser.getCurrentUser().getParseObject("preferences");
        Preferences generalPreferences = Preferences.getGeneralPreferences();

        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Envelope<List<Recipe>>> call = service.getSortedRecipes(BuildConfig.API_KEY,
                getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_CUISINE),
                getListRecommendation(currentPreferences, generalPreferences, Preferences.KEY_USER_DIET),
                "random",
                String.valueOf(currentPreferences.getMaxTime().intValue()),
                "true");
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                List<Recipe> res = response.body().results;
                for(Recipe item : res) {
                    Log.i("RECOMMENDATION", item.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Log.e("RECOMMENDATION", "Failed " + t);
            }
        });
        return null;
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
}
