package com.example.recipeapp.Retrofit;

import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.API.RecipeTitle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {
    String RANDOM_ORDER = "random";
    String FAILURE = "failure";
    String API_KEY = "apiKey";
    String ID = "id";
    String QUERY = "query";
    String NUMBER = "number";
    String TITLE_MATCH = "titleMatch";
    String CUISINE = "cuisine";
    String EXCLUDE_CUISINE = "excludeCuisine";
    String INCLUDE_INGREDIENTS = "includeIngredients";
    String EXCLUDE_INGREDIENTS = "excludeIngredients";
    String TYPE = "type";
    String MAX_READY_TIME = "maxReadyTime";
    String ADD_RECIPE_INFORMATION = "addRecipeInformation";
    String RECIPE_INFORMATION_VALUE = "true";
    String SORT = "sort";
    String DIET = "diet";

    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getAllRecipes(@Query(API_KEY) String apiKey);

    @GET("/recipes/{id}/information")
    Call<Recipe> getRecipeById(@Path(ID) long id, @Query(API_KEY) String apiKey);

    @GET("/food/ingredients/autocomplete")
    Call<List<Ingredient>> getIngredientAutocomplete(@Query(API_KEY) String apiKey,
                                                     @Query(QUERY) String query,
                                                     @Query(NUMBER) int number);

    @GET("/recipes/autocomplete")
    Call<List<RecipeTitle>> getTitleAutocomplete(@Query(API_KEY) String apiKey,
                                                 @Query(QUERY) String query,
                                                 @Query(NUMBER) int number);

    @GET("/food/ingredients/{id}/substitutes")
    Call<SubEnvelope<List<String>>> getIngredientSubstitute(@Path(ID) Long id,
                                                            @Query(API_KEY) String apiKey
                                                 );

    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getRecipesWithFilters(@Query(API_KEY) String apiKey,
                                             @Query(TITLE_MATCH) String titleMatch,
                                             @Query(CUISINE) String cuisine,
                                             @Query(EXCLUDE_CUISINE) String excludeCuisine,
                                             @Query(INCLUDE_INGREDIENTS) String includeIngredients,
                                             @Query(EXCLUDE_INGREDIENTS) String excludeIngredients,
                                             @Query(TYPE) String type,
                                             @Query(MAX_READY_TIME) String maxReadyTime,
                                             @Query(ADD_RECIPE_INFORMATION) String addRecipeInformation
                                             );

    @GET("/recipes/{id}/tasteWidget.json")
    Call<Taste> getTasteById(@Path("id") Long id,@Query(API_KEY) String apiKey);

    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getSortedRecipes(@Query(API_KEY) String apiKey,
                                                  @Query(CUISINE) String cuisine,
                                                  @Query(DIET) String diet,
                                                  @Query(SORT) String sortOrder,
                                                  @Query(MAX_READY_TIME) String maxReadyTime,
                                                  @Query(NUMBER) int numberOfResults,
                                                  @Query(ADD_RECIPE_INFORMATION) String addRecipeInformation
    );

}
