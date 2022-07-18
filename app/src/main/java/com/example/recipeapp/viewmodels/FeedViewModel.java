package com.example.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.SearchApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;

import java.util.List;

public class FeedViewModel extends ViewModel {

    private RecipesRepository repo;
    public LiveData<List<Recipe>> allRecipes;

    public FeedViewModel() {
        repo = RecipesRepository.getRepository();
    }

    public void fetch(RecipesRepository.DataSource dataSource, SearchApiCallParams params) {
        allRecipes = repo.fetch(dataSource, params);
    }
}
