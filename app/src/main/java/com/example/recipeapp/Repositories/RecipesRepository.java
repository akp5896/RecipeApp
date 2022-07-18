package com.example.recipeapp.Repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.SearchApiCallParams;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Room.RecipeDao;
import com.example.recipeapp.Room.RecipeDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static final String TAG = "RecipesRepo";
    private static RecipesRepository recipesRepository;
    private final RecipeDao recipeDao = RecipeDatabase.getRecipeDatabase().recipeDao();;
    private LiveData<List<Recipe>> bookmarkedRecipes;
    ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);

    public static RecipesRepository getRepository() {
        if(recipesRepository == null) {
            recipesRepository = new RecipesRepository();
        }
        return recipesRepository;
    }

    public LiveData<List<Recipe>> fetch(DataSource dataSource, SearchApiCallParams params) {
        switch (dataSource) {
            case LOCAL_SQL_DB:
                return fetchLocal();
            case API_CALL:
                return fetchApi(params);
        }
        return null;
    }

    public LiveData<List<Recipe>> fetchLocal() {
        bookmarkedRecipes = recipeDao.getRecipes();
        return bookmarkedRecipes;
    }

    public LiveData<List<Recipe>> fetchApi(SearchApiCallParams params) {
        Call<Envelope<List<Recipe>>> call = service.getRecipesWithFilters(BuildConfig.API_KEY, params.getTitle(), params.getCuisine(), params.getExcludeCuisine(),
                params.getIncluded(), params.getExcluded(),
                params.getType(), params.getTime(), RecipeApi.RECIPE_INFORMATION_VALUE);
        bookmarkedRecipes = new MutableLiveData<>();

        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(@NonNull Call<Envelope<List<Recipe>>> call, @NonNull Response<Envelope<List<Recipe>>> response) {
                if(bookmarkedRecipes instanceof MutableLiveData) {
                    ((MutableLiveData)bookmarkedRecipes).postValue(response.body().results);
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                Log.e(TAG, "Recipes search failed: " + t);
            }
        });
        return bookmarkedRecipes;
    }

    public void getTaste(Long id, Callback<Taste> callback) {
        Call<Taste> call = service.getTasteById(id, BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public LiveData<Recipe> reloadRecipe(Long id) {
        Call<Recipe> recipeById = service.getRecipeById(id, BuildConfig.API_KEY);
        MutableLiveData<Recipe> recipeData = new MutableLiveData<>();
        recipeById.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                recipeData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "Something went wrong: " + t);
            }
        });
        return recipeData;
    }

    public void getIngredientAutocomplete(String query, Callback<List<Ingredient>> callback) {
        Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
        call.enqueue(callback);
    }

    public void bookmark(Recipe recipe, BookmarkCallback callback) {
        executor.execute(() -> {
            RecipeDatabase recipeDatabase = RecipeDatabase.getRecipeDatabase();
            if (recipe.isBookmarked == null) {
                recipeDatabase.runInTransaction(() -> {
                    recipe.isBookmarked = (recipeDatabase.recipeDao().getRecipeById(recipe.id) > 0);
                    changeBookmark(recipe, callback);
                });
            } else {
                changeBookmark(recipe, callback);
            }
        });
    }

    private void changeBookmark(Recipe recipe, BookmarkCallback callback) {
        RecipeDatabase recipeDatabase = RecipeDatabase.getRecipeDatabase();
        recipe.isBookmarked = !recipe.isBookmarked;
        if(recipe.isBookmarked) {
            recipeDatabase.runInTransaction(() -> recipeDatabase.recipeDao().insertRecipe(recipe));
            handler.post(() -> callback.onBookmarkResult(BookmarkCallback.BookmarkResult.BOOKMARKED));
        }
        else {
            recipeDatabase.runInTransaction(() -> recipeDatabase.recipeDao().delete(recipe));
            handler.post(() -> callback.onBookmarkResult(BookmarkCallback.BookmarkResult.UNBOOKMARKED));
        }
    }

    public interface BookmarkCallback {
        void onBookmarkResult(BookmarkResult result);
        enum BookmarkResult {
            BOOKMARKED,
            UNBOOKMARKED
        }
    }

    public enum DataSource {
        LOCAL_SQL_DB,
        API_CALL
    }
}
