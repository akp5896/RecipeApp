package com.example.recipeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Adapters.AutoCompleteAdapter;
import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Adapters.IngredientsAdapter;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.databinding.FragmentSuggestBinding;
import com.example.recipeapp.viewmodels.LoginViewModel;
import com.example.recipeapp.viewmodels.SuggestViewModel;
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
    FragmentSuggestBinding binding;

    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SuggestViewModel viewModel = ViewModelProviders.of(this).get(SuggestViewModel.class);
        binding.setViewModel(viewModel);

        binding.edIngredients.setAdapter(
                new AutoCompleteAdapter<Ingredient>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        (query, callback) -> {
                            RecipesRepository.getRepository().getIngredientAutocomplete(query, callback);
                        }));

        IngredientFilterAdapter iHaveAdapter = new IngredientFilterAdapter(viewModel.getiHave(), R.layout.item);
        binding.rvIngredients.setAdapter(iHaveAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        binding.rvIngredients.setLayoutManager(layoutManager);

        binding.btnAdd.setOnClickListener(v -> {
            viewModel.getiHave().add(binding.edIngredients.getText().toString());
            iHaveAdapter.notifyItemInserted(viewModel.getiHave().size() - 1);
            binding.edIngredients.setText(null);
        });

        viewModel.searchResult.observe(getViewLifecycleOwner(), recipe -> {
            if(recipe != null) {
                Intent i = new Intent(getContext(), DetailsActivity.class);
                i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(recipe));
                startActivity(i);
            }
            else {
                Toast.makeText(getContext(), "Cannot find anything", Toast.LENGTH_LONG).show();
            }
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