package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Parse.ParseRecipeData;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Room.RecipeDao;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static final String TAG = "RecipesRepo";
    private static RecipesRepository recipesRepository;
    RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
    private LiveData<List<Recipe>> bookmarkedRecipes;

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

    public LiveData<List<Recipe>> fetchApi(ApiCallParams params) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Envelope<List<Recipe>>> call = service.getRecipesWithFilters(BuildConfig.API_KEY, params.getTitle(), params.getCuisine(), params.getExcludeCuisine(),
                params.getIncluded(), params.getExcluded(),
                params.getType(), params.getTime(), RecipeApi.RECIPE_INFORMATION_VALUE);
        bookmarkedRecipes = new MutableLiveData<>();

        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(@NonNull Call<Envelope<List<Recipe>>> call, @NonNull Response<Envelope<List<Recipe>>> response) {
                if(bookmarkedRecipes instanceof MutableLiveData) {
                    ((MutableLiveData<List<Recipe>>)bookmarkedRecipes).postValue(response.body().results);
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Log.e(TAG, "Recipes search failed: " + t);
            }
        });
        return bookmarkedRecipes;
    }

    public MutableLiveData<List<Recipe>> fetchParse() {
        return fetchParse(null);
    }

    public void getIngredientAutocomplete(String query, Callback<List<Ingredient>> callback) {
        Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
        call.enqueue(callback);
    }

    public MutableLiveData<List<Recipe>> fetchParse(String username) {
        ParseQuery<ParseRecipeData> query = ParseQuery.getQuery(ParseRecipeData.class);
        query.setLimit(10);
        if(username != null) {
            query.whereEqualTo(ParseRecipeData.KEY_AUTHOR, username);
        }
        query.orderByDescending(ParseObject.KEY_CREATED_AT);
        MutableLiveData<List<Recipe>> response = new MutableLiveData<>();
        query.findInBackground(new FindCallback<ParseRecipeData>() {
            @Override
            public void done(List<ParseRecipeData> recipes, ParseException e) {
                List<Recipe> result = new ArrayList<>();
                for(ParseRecipeData recipeData : recipes) {
                    Recipe recipe = new Recipe(recipeData.getTitle(),
                            "",
                            0L,
                            recipeData.getTime(),
                            0d,
                            0d,
                            recipeData.getServings(),
                            recipeData.getSteps(),
                            recipeData.getIngredients(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            recipeData.getSummary());
                    result.add(recipe);
                }
                response.postValue(result);
            }
        });
        return response;
    }
}
