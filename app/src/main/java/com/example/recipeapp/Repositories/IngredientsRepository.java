package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Retrofit.SubEnvelope;
import com.roughike.swipeselector.SwipeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsRepository {
    private static final String TAG = "Ingredients repository";
    private static IngredientsRepository ingredientsRepository;

    public static IngredientsRepository getRepository() {
        if(ingredientsRepository == null) {
            ingredientsRepository = new IngredientsRepository();
        }
        return ingredientsRepository;
    }

    public MutableLiveData<SubEnvelope<List<String>>> getIngredientSubstitutes(Long id) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<SubEnvelope<List<String>>> call = service.getIngredientSubstitute(id, BuildConfig.API_KEY);
        MutableLiveData<SubEnvelope<List<String>>> subs = new MutableLiveData<>();
        call.enqueue(new Callback<SubEnvelope<List<String>>>() {
            @Override
            public void onResponse(Call<SubEnvelope<List<String>>> call, Response<SubEnvelope<List<String>>> response) {
                subs.postValue(response.body());
            }

            @Override
            public void onFailure(Call<SubEnvelope<List<String>>> call, Throwable t) {
                Log.i(TAG, "Couldn't retrieve substitutes: " + t);
            }
        });
        return subs;
    }
}
