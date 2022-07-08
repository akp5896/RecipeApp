package com.example.recipeapp.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Room.BookmarksRepository;
import com.example.recipeapp.Room.RecipeDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedViewModel extends ViewModel {

    public MutableLiveData<DataSource> dataSource = new MutableLiveData<>();
    private final BookmarksRepository repo;
    public final LiveData<List<Recipe>> allRecipes;

    public FeedViewModel() {
        repo = new BookmarksRepository(RecipeDatabase.getRecipeDatabase().recipeDao());
        allRecipes = repo.bookmarkedRecipes;
    }

    // Currently contains only one value, but since I'm already have to load messages with a different way in the search fragment, there will be more
    public enum DataSource {
        LOCAL_SQL_STORAGE
    }
}
