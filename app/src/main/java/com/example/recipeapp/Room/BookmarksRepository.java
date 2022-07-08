package com.example.recipeapp.Room;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.Models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookmarksRepository {

    private final RecipeDao recipeDao;
    public LiveData<List<Recipe>> bookmarkedRecipes;

    public BookmarksRepository(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
        bookmarkedRecipes = recipeDao.getRecipes();
    }
}
