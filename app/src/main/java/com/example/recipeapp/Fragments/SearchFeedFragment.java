package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapp.Adapters.RecipesAdapter;
import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Room.RecipesRepository;
import com.example.recipeapp.viewmodels.FeedViewModel;

import java.util.List;

public class SearchFeedFragment extends FeedFragment {

    private ApiCallParams params;

    public SearchFeedFragment() {
        // Required empty public constructor
    }

    public static SearchFeedFragment newInstance(ApiCallParams params) {
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
}