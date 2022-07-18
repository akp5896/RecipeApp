package com.example.recipeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import android.view.View;

import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;

import java.util.List;

/**
 * Extension of FeedFragemnt used to show the recipes retrieved from local storage, available offline.
 */
public class BookmarkFeedFragment extends FeedFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.fetch(RecipesRepository.DataSource.LOCAL_SQL_DB, null);
        viewModel.allRecipes.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                adapter.updateList(recipes);
            }
        });
    }
}