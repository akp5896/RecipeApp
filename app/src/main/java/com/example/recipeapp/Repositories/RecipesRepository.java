package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static final String TAG = "RecipesRepo";
    private static RecipesRepository recipesRepository;
    RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);

    public static RecipesRepository getRepository() {
        if(recipesRepository == null) {
            recipesRepository = new RecipesRepository();
        }
        return recipesRepository;
    }

    public void reloadRecipe(Long id, Callback<Recipe> callback) {
        Call<Recipe> recipeById = service.getRecipeById(id, BuildConfig.API_KEY);
        recipeById.enqueue(callback);
    }
}
