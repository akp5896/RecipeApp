package com.example.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Room.RecipesRepository;

import java.util.List;

public class FeedViewModel extends ViewModel {

    private RecipesRepository repo;
    public LiveData<List<Recipe>> allRecipes;

    public ApiCallParams params;
    public DataSource dataSource;

    public FeedViewModel() {
        repo = new RecipesRepository();
    }

    public void fetch(DataSource dataSource, ApiCallParams params) {
        switch (dataSource) {
            case API_CALL:
                fetchApi(params);
                break;
            case PARSE_DB:
                fetchParse();
                break;
        }
    }

    private void fetchParse() {
        allRecipes = repo.fetchParse();
    }

    private void fetchApi(ApiCallParams params) {
        allRecipes = repo.fetchApi(params);
    }

    public enum DataSource {
        LOCAL_SQL_DB,
        API_CALL,
        PARSE_DB
    }
}
