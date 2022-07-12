package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityIngridientsBinding;

import org.parceler.Parcels;

import java.util.List;
import java.util.stream.Collectors;

public class IngredientsActivity extends AppCompatActivity {

    private static final String TAG = "INGR ACTIVITY";

    public static final String RECIPE = "recipe";

    List<Ingredient> ingredients;

    ActivityIngridientsBinding binding;
    IngredientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngridientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Recipe recipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE));
        ingredients = recipe.ingredients();

        adapter = new IngredientsAdapter(ingredients);

        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        binding.rvIngredients.setAdapter(adapter);
    }
}