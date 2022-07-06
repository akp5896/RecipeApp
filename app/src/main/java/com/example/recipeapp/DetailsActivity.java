package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    Recipe recipe;
    List<String> steps = new ArrayList<>();
    public static final String RECIPE = "recipe";

    StepsAdapter stepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE));

        Glide.with(this).load(recipe.getImage()).into(binding.ivImage);
        binding.tvServings.setText(String.format("%s\nservings", recipe.getServings().toString()));
        binding.tvTime.setText(String.format("%s minutes", recipe.getTimeToCook().toString()));
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(this));


        stepsAdapter = new StepsAdapter(steps, R.layout.item_list);
        binding.rvSteps.setAdapter(stepsAdapter);

        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Recipe> recipeById = service.getRecipeById(recipe.getId(), BuildConfig.API_KEY);
        recipeById.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                recipe.setAnalyzedInstructions(response.body().getAnalyzedInstructions());
                recipe.setIngredients(response.body().getIngredients());
                for(Step item : recipe.getAnalyzedInstructions().get(0).results) {
                    steps.add(item.number + ". " + item.step);
                }
                stepsAdapter.notifyItemRangeChanged(0, steps.size());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "Something went wrong: " + t);
            }
        });

        binding.btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, IngredientsActivity.class);
                i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(recipe));
                startActivity(i);
            }
        });

        binding.btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, IngredientsActivity.class);
                i.putExtra(IngredientsActivity.RECIPE, recipe.getTitle());
                startActivity(i);
            }
        });

        binding.options.like.setOnClickListener(this::onLike);
        binding.options.review.setOnClickListener(this::onReview);
    }

    private void onLike(View v) {
        Toast.makeText(DetailsActivity.this, R.string.liked, Toast.LENGTH_SHORT).show();
        recipe.getTaste(new Callback<Taste>() {
            @Override
            public void onResponse(Call<Taste> call, Response<Taste> response) {
                Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.PREFERENCES);
                preferences.updatePreferences(recipe, response.body());
                preferences.saveInBackground();
            }

            @Override
            public void onFailure(Call<Taste> call, Throwable t) {
                Log.e(TAG, "Failure : " + t);
            }
        });
    }

    private void onReview(View v) {
        Intent i = new Intent(DetailsActivity.this, ReviewsActivity.class);
        i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(recipe));
        startActivity(i);
    }
}