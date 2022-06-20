package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.function.ObjIntConsumer;

import okhttp3.Headers;

public class RecipeClient {
    public static final String BASE_URL = "https://api.spoonacular.com";
    public static final String API_KEY = BuildConfig.API_KEY;

    public void getAllRecipes(JsonHttpResponseHandler handler) {
        String apiUrl = BASE_URL + "/recipes/complexSearch";
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(apiUrl, params, handler);
    }

    public void getRecipeById(Long id, JsonHttpResponseHandler handler) {
        @SuppressLint("DefaultLocale")
        String apiUrl = String.format(BASE_URL + "/recipes/{%d}/information", id);
        RequestParams params = new RequestParams();
        params.put("apiKey", API_KEY);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(apiUrl, params, handler);
    }



}
