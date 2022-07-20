package com.example.recipeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.FragmentSuggestBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestFragment extends Fragment {

    private static final String TAG = "Suggest activity";
    public static final int MINIMIZE_MISSING = 2;
    FragmentSuggestBinding binding;
    IngredientFilterAdapter iHaveAdapter;
    List<String> iHave = new ArrayList<>();

    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        binding.edIngredients.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        iHaveAdapter = new IngredientFilterAdapter(iHave, R.layout.item);
        binding.rvIngredients.setAdapter(iHaveAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.rvIngredients.setLayoutManager(layoutManager);

        binding.btnAdd.setOnClickListener(v -> {
            iHave.add(binding.edIngredients.getText().toString());
            iHaveAdapter.notifyItemInserted(iHave.size() - 1);
            binding.edIngredients.setText(null);
        });

        binding.btnSuggest.setOnClickListener(v -> {
            Call<List<Recipe>> call = service.getRecipeByIngredients(BuildConfig.API_KEY, MINIMIZE_MISSING, String.join(",", iHave));
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if(response.body() != null && response.body().size() != 0) {
                        Call<Recipe> detailsCall = service.getRecipeById(response.body().get(0).getId(), BuildConfig.API_KEY);
                        detailsCall.enqueue(new Callback<Recipe>() {
                            @Override
                            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                                Intent i = new Intent(getContext(), DetailsActivity.class);
                                i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(response.body()));
                                startActivity(i);
                            }

                            @Override
                            public void onFailure(Call<Recipe> call, Throwable t) {
                                Log.e(TAG, "Recipes search failed: " + t);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.e(TAG, "Recipes search failed: " + t);
                }
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuggestBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}