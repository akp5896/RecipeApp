package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.Queue;
import java.util.function.ObjIntConsumer;

import okhttp3.Headers;

public class RecipeClient {
    public static final String BASE_URL = "https://api.spoonacular.com";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final AsyncHttpClient client = new AsyncHttpClient();
    public static RecipeClient recipeClient;

    public void getAllRecipes(JsonHttpResponseHandler handler) {
        String apiUrl = BASE_URL + "/recipes/complexSearch";
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        client.get(apiUrl, params, handler);
    }

    public void getRecipesWithFilters(JsonHttpResponseHandler handler,
                                      String title,
                                      String cuisine,
                                      String excludeCuisine,
                                      String includeIngridients,
                                      String excludeIngredients,
                                      String type,
                                      String maxReadyTime) {
        String apiUrl = BASE_URL + "/recipes/complexSearch";
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        putWithEmptyCheck(params, "titleMatch", title);
        putWithEmptyCheck(params, "cuisine", cuisine);
        putWithEmptyCheck(params, "excludeCuisine", excludeCuisine);
        putWithEmptyCheck(params, "includeIngredients", includeIngridients);
        putWithEmptyCheck(params, "excludeIngredients", excludeIngredients);
        putWithEmptyCheck(params, "type", type);
        putWithEmptyCheck(params, "maxReadyTime", maxReadyTime);
        params.put("addRecipeInformation", true);
        client.get(apiUrl, params, handler);
    }

    private void putWithEmptyCheck(RequestParams params, String key, String value) {
        if(value != null && !value.equals("")) {
            params.put(key, value);
        }
    }

    public void getRecipeById(Long id, JsonHttpResponseHandler handler) {
        @SuppressLint("DefaultLocale")
        String apiUrl = String.format(BASE_URL + "/recipes/%d/information", id);
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        client.get(apiUrl, params, handler);
    }

    public void getIngredientAutocomplete(String query, JsonHttpResponseHandler handler) {
        String apiUrl = BASE_URL + "/food/ingredients/autocomplete";
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        params.put("query", query);
        params.put("number", 5);
        client.get(apiUrl, params, handler);
    }

    public static RecipeClient getInstance() {
        if(recipeClient == null) {
            recipeClient = new RecipeClient();
        }
        return recipeClient;
    }

}
