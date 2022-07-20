package com.example.recipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.ActivityAddRecipeBinding;
import com.example.recipeapp.viewmodels.AddRecipeViewModel;
import com.example.recipeapp.viewmodels.StepViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class AddRecipeActivity extends AppCompatActivity {
    ActivityAddRecipeBinding binding;
    AddRecipeViewModel viewModel;
    IngredientFilterAdapter ingredientsAdapter;
    StepsAdapter stepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(AddRecipeViewModel.class);
        binding.setViewModel(viewModel);

        ingredientsAdapter = new IngredientFilterAdapter(viewModel.getIngredients(), R.layout.item);
        binding.rvIngredients.setAdapter(ingredientsAdapter);
        binding.rvIngredients.setLayoutManager(getFlexboxLayoutManager());

        viewModel.addedIngredient.observe(this, s -> {
            ingredientsAdapter.notifyItemInserted(viewModel.getIngredients().size() - 1);
            binding.edExclude.setText(null);
        });

        stepsAdapter = new StepsAdapter();
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSteps.setAdapter(stepsAdapter);

        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);

        binding.edExclude.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
                            call.enqueue(callback);
                        }));

        binding.spinnerCuisine.setOptions(Arrays.asList(getResources().getStringArray(R.array.cuisines)));

        viewModel.addedStep.observe(this, step -> {
            stepsAdapter.add(new StepViewModel(step.getStep(), step.getNumber()));
            binding.edSteps.setText(null);
        });
    }

    @NonNull
    private FlexboxLayoutManager getFlexboxLayoutManager() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        return layoutManager;
    }
}