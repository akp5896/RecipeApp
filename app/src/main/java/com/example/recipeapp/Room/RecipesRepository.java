package com.example.recipeapp.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.MainActivity;
import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.viewmodels.FeedViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static final String TAG = "RecipesRepo";
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> bookmarkedRecipes;

    public LiveData<List<Recipe>> fetchLocal() {
        this.recipeDao = RecipeDatabase.getRecipeDatabase().recipeDao();
        bookmarkedRecipes = recipeDao.getRecipes();
        return bookmarkedRecipes;
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
<<<<<<< HEAD
=======

    private String putWithEmptyCheck(CharSequence chars) {
        if(chars == null || chars.toString().equals(""))
            return null;
        return chars.toString();
    }
>>>>>>> 7911c1e... call to api moved to viewmodel
}
