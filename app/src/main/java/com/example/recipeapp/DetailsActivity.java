package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Models.Ingredient;
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
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.example.recipeapp.viewmodels.DetailsViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    public static final String RECIPE = "recipe";
    public static final String INGREDIENTS = "ingredients";

    Recipe recipe;
    DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        recipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE));
        viewModel = new DetailsViewModel(recipe);
        binding.setViewModel(viewModel);

        viewModel.showIngredients.observe(this, ingredients -> {
            Intent i = new Intent(DetailsActivity.this, IngredientsActivity.class);
            i.putExtra(DetailsActivity.INGREDIENTS, Parcels.wrap(ingredients));
            startActivity(i);
        });

        binding.options.like.setOnClickListener(this::onLike);
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
}