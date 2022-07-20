package com.example.recipeapp.viewmodels;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestViewModel extends ViewModel {
    private static final String TAG = "Suggest view model";
    public static final int MINIMIZE_MISSING = 2;
    List<String> iHave = new ArrayList<>();
    public MutableLiveData<Recipe> searchResult = new MutableLiveData<>();

    public void suggestRecipes() {
        RecipesRepository.getRepository().suggestRecipes(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                searchResult.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "Recipes search failed: " + t);
                searchResult.postValue(null);
            }
        }, MINIMIZE_MISSING, String.join(",", iHave));
    }

    public List<String> getiHave() {
        return iHave;
    }
}
