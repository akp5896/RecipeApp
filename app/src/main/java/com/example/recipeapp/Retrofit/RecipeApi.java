package com.example.recipeapp.Retrofit;

import com.example.recipeapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getAllRecipes(@Query("apiKey") String apiKey);

    @GET("/recipes/%d/information")
    Call<Recipe> getRecipeById(@Query("apiKey") String apiKey,
                                               @Path("id") long id);

    @GET("/food/ingredients/autocomplete")
    Call<List<Ingredient>> getIngredientAutocomplete(@Query("apiKey") String apiKey,
                                           @Query("query") String query,
                                           @Query("number") int number);



}
