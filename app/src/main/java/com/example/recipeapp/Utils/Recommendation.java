package com.example.recipeapp.Utils;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.ParseUser;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;


//        @Query("cuisine") String cuisine,
//        @Query("excludeCuisine") String excludeCuisine,
//        @Query("maxReadyTime") String maxReadyTime,
//        @Query("diet") String diet
public class Recommendation {
    public static List<Recipe> recommend() {
        Preferences currentPreferences = (Preferences) ParseUser.getCurrentUser().getParseObject("preferences");
        Preferences generalPreferences = Preferences.getGeneralPreferences();


        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        return null;
    }
}
