package com.example.recipeapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Adapters.RecipesAdapter;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.ParseApplication;
import com.example.recipeapp.R;
import com.example.recipeapp.Room.RecipeDatabase;
import com.example.recipeapp.databinding.FragmentFeedBinding;
import com.example.recipeapp.databinding.RecipeItemBinding;
import com.example.recipeapp.viewmodels.FeedViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private static final String TAG = "FEED FRAGMENT";
    FragmentFeedBinding binding;
    RecipesAdapter adapter;
    List<Recipe> recipes = new ArrayList<>();

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public RecipesAdapter getAdapter() {
        return adapter;
    }

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(List<Recipe> recipes) {
        FeedFragment fragment = new FeedFragment();
        fragment.recipes.addAll(recipes);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(ViewModelProviders.of(this).get(FeedViewModel.class));
        binding.getViewModel().dataSource.observe(getViewLifecycleOwner(), new Observer<FeedViewModel.DataSource>() {
            @Override
            public void onChanged(FeedViewModel.DataSource dataSource) {
                Log.e(TAG, "this");
            }
        });
        adapter = new RecipesAdapter(recipes, getContext());
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}