package com.example.recipeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.databinding.ActivityIngridientsBinding;

import org.parceler.Parcels;

import java.util.List;

public class IngredientsActivity extends AppCompatActivity {

    private static final String TAG = "INGR ACTIVITY";

    List<Ingredient> ingredients;

    ActivityIngridientsBinding binding;
    IngredientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngridientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ingredients = Parcels.unwrap(getIntent().getParcelableExtra(DetailsActivity.INGREDIENTS));

        // Since there are limited number of call on free api, and a lot of ingredients, this line is used for testing.
        //ingredients = ingredients.stream().limit(2).collect(Collectors.toList());

        adapter = new IngredientsAdapter(ingredients);

        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        binding.rvIngredients.setAdapter(adapter);
    }
}