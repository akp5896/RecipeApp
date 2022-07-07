package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.Models.Ingredient;
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
    //Recipe recipe;
    List<String> steps = new ArrayList<>();
    public static final String RECIPE = "recipe";
    StepsAdapter stepsAdapter;

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

        viewModel.bookmarkToast.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer messageId) {
                Toast.makeText(DetailsActivity.this, messageId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}