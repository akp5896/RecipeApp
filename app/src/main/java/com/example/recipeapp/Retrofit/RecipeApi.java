package com.example.recipeapp.Retrofit;

import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.API.RecipeTitle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getAllRecipes(@Query("apiKey") String apiKey);

    @GET("/recipes/{id}/information")
    Call<Recipe> getRecipeById(@Path("id") long id, @Query("apiKey") String apiKey);

    @GET("/food/ingredients/autocomplete")
    Call<List<Ingredient>> getIngredientAutocomplete(@Query("apiKey") String apiKey,
                                                     @Query("query") String query,
                                                     @Query("number") int number);

    @GET("/recipes/autocomplete")
    Call<List<RecipeTitle>> getTitleAutocomplete(@Query("apiKey") String apiKey,
                                                 @Query("query") String query,
                                                 @Query("number") int number);

    @GET("/food/ingredients/{id}/substitutes")
    Call<SubEnvelope<List<String>>> getIngredientSubstitute(@Path("id") Long id,
                                                            @Query("apiKey") String apiKey
                                                 );

    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getRecipesWithFilters(@Query("apiKey") String apiKey,
                                             @Query("titleMatch") String titleMatch,
                                             @Query("cuisine") String cuisine,
                                             @Query("excludeCuisine") String excludeCuisine,
                                             @Query("includeIngredients") String includeIngredients,
                                             @Query("excludeIngredients") String excludeIngredients,
                                             @Query("type") String type,
                                             @Query("maxReadyTime") String maxReadyTime,
                                             @Query("addRecipeInformation") String addRecipeInformation
                                             );

}
