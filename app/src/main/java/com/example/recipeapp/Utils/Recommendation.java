package com.example.recipeapp.Utils;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.ParseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


//        @Query("cuisine") String cuisine,
//        @Query("excludeCuisine") String excludeCuisine,
//        @Query("maxReadyTime") String maxReadyTime,
//        @Query("diet") String diet
public class Recommendation {
//    @GET("/recipes/complexSearch")
//    Call<Envelope<List<Recipe>>> getSortedRecipes(@Query("apiKey") String apiKey,
//                                                  @Query("cuisine") String cuisine,
//                                                  @Query("excludeCuisine") String excludeCuisine,
//                                                  @Query("maxReadyTime") String maxReadyTime,
//                                                  @Query("addRecipeInformation") String addRecipeInformation,
//                                                  @Query("sort") String sortOrder,
//                                                  @Query("diet") String diet
//    );
    public static List<Recipe> recommend() {
//        Preferences currentPreferences = (Preferences) ParseUser.getCurrentUser().getParseObject("preferences");
//        Preferences generalPreferences = Preferences.getGeneralPreferences();
            return null;
    }
}
