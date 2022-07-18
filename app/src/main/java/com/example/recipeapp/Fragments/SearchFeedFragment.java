package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import android.view.View;

import com.example.recipeapp.Models.API.SearchApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Room.RecipesRepository;

import java.util.List;

public class SearchFeedFragment extends FeedFragment {

    private SearchApiCallParams params;

    public SearchFeedFragment() {
        // Required empty public constructor
    }

    public static SearchFeedFragment newInstance(SearchApiCallParams params) {
        SearchFeedFragment fragment = new SearchFeedFragment();
        fragment.params = params;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.fetch(RecipesRepository.DataSource.API_CALL, params);
        viewModel.allRecipes.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                adapter.updateList(recipes);
            }
        });
    }

    public void updateRecipeFeed(SearchApiCallParams params) {
        this.params = params;
        viewModel.fetch(RecipesRepository.DataSource.API_CALL, params);
    }
}