package com.example.flex3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("/recipes/complexSearch")
    Call<Envelope<List<Recipe>>> getAllRecipes(@Query("apiKey") String apiKey,
                                     @Query("includeIngredients") String type);
}
