package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.example.recipeapp.viewmodels.DetailsViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    public static final String RECIPE = "recipe";
    public static final String INGREDIENTS = "ingredients";

    DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        Recipe recipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE));
        viewModel = new DetailsViewModel(recipe);
        binding.setViewModel(viewModel);

        viewModel.showIngredients.observe(this, ingredients -> {
            Intent i = new Intent(DetailsActivity.this, IngredientsActivity.class);
            i.putExtra(DetailsActivity.INGREDIENTS, Parcels.wrap(ingredients));
            startActivity(i);
        });

        viewModel.numberOfLikes.observe(this, numberOfLikes -> binding.tvLikes.setText(String.valueOf(numberOfLikes)));
    }
}