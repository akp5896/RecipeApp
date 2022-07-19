package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

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

    public MutableLiveData<Integer> getLikes(Long id) {
        MutableLiveData<Integer> numberOfLikes = new MutableLiveData<>();
        ParseRecipe.findById(id, new FindCallback<ParseRecipe>() {
            @Override
            public void done(List<ParseRecipe> objects, ParseException e) {
                ParseRecipe parseRecipe = null;
                if(objects == null || objects.size() == 0) {
                    parseRecipe = new ParseRecipe();
                    parseRecipe.setId(id);
                }
                else {
                    parseRecipe = objects.get(0);
                }
                numberOfLikes.postValue(parseRecipe.getNumberOfLiked());
            }
        });
        return numberOfLikes;
    }

    public void reloadRecipe(Long id, Callback<Recipe> callback) {
        Call<Recipe> recipeById = service.getRecipeById(id, BuildConfig.API_KEY);
        recipeById.enqueue(callback);
    }
}
