package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.Adapters.ItemsAdapter;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityDetailsBinding;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    Recipe recipe;
    List<String> steps = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    ItemsAdapter stepsAdapter;
    ItemsAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        Glide.with(this).load(recipe.getImage()).into(binding.ivImage);
        binding.tvServings.setText(recipe.getServings().toString() + "\nservings");
        binding.tvTime.setText(recipe.getTimeToCook().toString() + " minutes");
        binding.rvIngredients.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(this));
        stepsAdapter = new ItemsAdapter(steps, R.layout.item_list);
        ingredientsAdapter = new ItemsAdapter(ingredients, R.layout.item_list);
        binding.rvSteps.setAdapter(stepsAdapter);
        binding.rvIngredients.setAdapter(ingredientsAdapter);

        RecipeClient.getInstance().getRecipeById(recipe.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Recipe.Steps(recipe, json.jsonObject);
                Recipe.Ingredients(recipe, json.jsonObject);
                steps.addAll(recipe.getAnalyzedInstructions());
                stepsAdapter.notifyItemRangeChanged(0, steps.size());
                ingredients.addAll(recipe.getIngredients());
                ingredientsAdapter.notifyItemRangeChanged(0, ingredients.size());
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Something went wrong: " + response);
            }
        });
    }
}