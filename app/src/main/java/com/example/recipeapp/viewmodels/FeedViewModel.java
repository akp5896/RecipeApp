package com.example.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Room.RecipesRepository;
import com.example.recipeapp.Room.RecipeDatabase;

import java.util.List;

public class FeedViewModel extends ViewModel {

    private RecipesRepository repo;
    public LiveData<List<Recipe>> allRecipes;

    public FeedViewModel() {
        repo = new RecipesRepository();
    }

    public void fetch(RecipesRepository.DataSource dataSource, ApiCallParams params) {
        allRecipes = repo.fetch(dataSource, params);
    }
}
