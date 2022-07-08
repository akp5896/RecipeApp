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
    public ApiCallParams params;
    public DataSource dataSource;

    public FeedViewModel() {
        repo = new RecipesRepository();
    }

    public void fetch() {
        switch (dataSource) {
            case LOCAL_SQL_DB:
                fetchLocal();
                break;
            case API_CALL:
                fetchApi(params);
                break;
        }
    }

    private void fetchLocal() {
        allRecipes = repo.fetchLocal();
    }

    private void fetchApi(ApiCallParams params) {
        allRecipes = repo.fetchApi(params);
    }

    public enum DataSource {
        LOCAL_SQL_DB,
        API_CALL
    }
}
