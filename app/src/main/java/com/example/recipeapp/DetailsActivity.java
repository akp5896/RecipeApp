package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.example.recipeapp.viewmodels.DetailsViewModel;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    public static final String RECIPE = "recipe";

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
            i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(ingredients));
            startActivity(i);
        });

        viewModel.bookmarkToast.observe(this, messageId -> Toast.makeText(DetailsActivity.this, messageId, Toast.LENGTH_SHORT).show());

        binding.options.like.setOnClickListener(v -> viewModel.onLike());
        viewModel.liked.observe(this, aBoolean -> Toast.makeText(DetailsActivity.this, R.string.liked, Toast.LENGTH_SHORT).show());
    }
}