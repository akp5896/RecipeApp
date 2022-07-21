package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Adapters.RecipesAdapter;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.FragmentFeedBinding;
import com.example.recipeapp.viewmodels.FeedViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends LeftSwipeDrawerFragment {
    FragmentFeedBinding binding;
    RecipesAdapter adapter;
    List<Recipe> recipes = new ArrayList<>();
    FeedViewModel viewModel;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public RecipesAdapter getAdapter() {
        return adapter;
    }

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
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
        viewModel = ViewModelProviders.of(requireActivity()).get(FeedViewModel.class);
        binding.setViewModel(viewModel);
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