package com.example.recipeapp.Repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.RecipeTitle;
import com.example.recipeapp.Models.API.SearchApiCallParams;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.ApiCallParams;
import com.example.recipeapp.Models.API.RecipeTitle;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Like;
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Parse.ParseRecipeData;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Retrofit.Envelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Room.RecipeDao;
import com.example.recipeapp.Room.RecipeDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static final String TAG = "RecipesRepo";
    private static RecipesRepository recipesRepository;
    private final RecipeDao recipeDao = RecipeDatabase.getRecipeDatabase().recipeDao();
    ;
    private LiveData<List<Recipe>> bookmarkedRecipes;
    ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);

    public static RecipesRepository getRepository() {
        if (recipesRepository == null) {
            recipesRepository = new RecipesRepository();
        }
        return recipesRepository;
    }

    public void getIngredientAutocomplete(String query, Callback<List<Ingredient>> callback) {
        Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
        call.enqueue(callback);
    }

    public void getTitleAutocomplete(String query, Callback<List<RecipeTitle>> callback) {
        Call<List<RecipeTitle>> call = service.getTitleAutocomplete(BuildConfig.API_KEY, query, 5);
        call.enqueue(callback);
    }

    public LiveData<List<Recipe>> fetch(DataSource dataSource, SearchApiCallParams params) {
        switch (dataSource) {
            case LOCAL_SQL_DB:
                return fetchLocal();
            case API_CALL:
                return fetchApi(params);
            case PARSE_DB:
                return fetchParse();
        }
        return null;
    }

    public LiveData<List<Recipe>> fetchLocal() {
        bookmarkedRecipes = recipeDao.getRecipes();
        return bookmarkedRecipes;
    }

    public LiveData<List<Recipe>> fetchApi(SearchApiCallParams params) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Envelope<List<Recipe>>> call = service.getRecipesWithFilters(BuildConfig.API_KEY, params.getTitle(), params.getCuisine(), params.getExcludeCuisine(),
                params.getIncluded(), params.getExcluded(),
                params.getType(), params.getTime(), null, null, RecipeApi.RECIPE_INFORMATION_VALUE);
        bookmarkedRecipes = new MutableLiveData<>();

        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(@NonNull Call<Envelope<List<Recipe>>> call, @NonNull Response<Envelope<List<Recipe>>> response) {
                if (bookmarkedRecipes instanceof MutableLiveData) {
                    ((MutableLiveData<List<Recipe>>) bookmarkedRecipes).postValue(response.body().results);
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
        if (recipe.isBookmarked) {
            recipeDatabase.runInTransaction(() -> recipeDatabase.recipeDao().insertRecipe(recipe));
            handler.post(() -> callback.onBookmarkResult(BookmarkCallback.BookmarkResult.BOOKMARKED));
        } else {
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

    public MutableLiveData<List<Recipe>> fetchParse() {
        return fetchParse(null);
    }

    public MutableLiveData<List<Ingredient>> getIngredientAutocomplete(String query) {
        Call<List<Ingredient>> call = service.getIngredientAutocomplete(BuildConfig.API_KEY, query, 5);
        MutableLiveData<List<Ingredient>> result = new MutableLiveData<>();
        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                result.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.e(TAG, "Autocomplete search failed: " + t);
            }
        });
        return result;
    }

    public MutableLiveData<List<Pair<String, Long>>> getNearbyRecipes(ParseGeoPoint parseGeoPoint) {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereNear(Like.KEY_LOCATION, parseGeoPoint);
        MutableLiveData<List<Pair<String, Long>>> recipesNearby = new MutableLiveData<>();
        query.findInBackground((likes, e) -> {
            Map<Long, List<Like>> nearRecipes =
                    likes.stream().collect(Collectors.groupingBy(w -> w.getLikeTo()));
            List<Long> recipeIds = new ArrayList<>(nearRecipes.keySet());
            Collections.sort(recipeIds, (o1, o2) -> Integer.compare(nearRecipes.get(o2).size(), nearRecipes.get(o1).size()));
            List<Pair<String, Long>> recipes = new ArrayList<>();
            for (Long item : recipeIds) {
                recipes.add(new Pair<>(nearRecipes.get(item).get(0).getTitle(), item));
            }
            recipesNearby.postValue(recipes);
        });
        return recipesNearby;
    }

    public MutableLiveData<List<Recipe>> fetchParse(String username) {
        ParseQuery<ParseRecipeData> query = ParseQuery.getQuery(ParseRecipeData.class);
        query.setLimit(10);
        if (username != null) {
            query.whereEqualTo(ParseRecipeData.KEY_AUTHOR, username);
        }
        query.orderByDescending(ParseObject.KEY_CREATED_AT);
        MutableLiveData<List<Recipe>> response = new MutableLiveData<>();
        query.findInBackground(new FindCallback<ParseRecipeData>() {
            @Override
            public void done(List<ParseRecipeData> recipes, ParseException e) {
                List<Recipe> result = new ArrayList<>();
                for (ParseRecipeData recipeData : recipes) {
                    Recipe recipe = new Recipe(recipeData.getTitle(),
                            getUri(recipeData.getMedia()),
                            0L,
                            recipeData.getTime(),
                            0d,
                            0d,
                            recipeData.getServings(),
                            recipeData.getSteps(),
                            recipeData.getIngredients(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            recipeData.getSummary());
                    result.add(recipe);
                }
                response.postValue(result);
            }
        });
        return response;
    }

    private String getUri(ParseFile media) {
        if (media == null) {
            return "";
        }
        return media.getUrl();
    }

    public MutableLiveData<List<RecipeTitle>> getTitleAutocomplete(String query) {
        Call<List<RecipeTitle>> call = service.getTitleAutocomplete(BuildConfig.API_KEY, query, 5);
        MutableLiveData<List<RecipeTitle>> result = new MutableLiveData<>();
        call.enqueue(new Callback<List<RecipeTitle>>() {
            @Override
            public void onResponse(Call<List<RecipeTitle>> call, Response<List<RecipeTitle>> response) {
                result.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<RecipeTitle>> call, Throwable t) {
                Log.e(TAG, "Autocomplete search failed: " + t);
            }
        });
        return result;
    }

    public MutableLiveData<Integer> getLikes(Long id) {
        MutableLiveData<Integer> numberOfLikes = new MutableLiveData<>();
        ParseRecipe.findById(id, new FindCallback<ParseRecipe>() {
            @Override
            public void done(List<ParseRecipe> objects, ParseException e) {
                ParseRecipe parseRecipe = null;
                if (objects == null || objects.size() == 0) {
                    parseRecipe = new ParseRecipe();
                    parseRecipe.setId(id);
                } else {
                    parseRecipe = objects.get(0);
                }
                numberOfLikes.postValue(parseRecipe.getNumberOfLiked());
            }
        });
        return numberOfLikes;
    }

    public void suggestRecipes(Callback<Recipe> callback, int mode, String ingredients) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<List<Recipe>> call = service.getRecipeByIngredients(BuildConfig.API_KEY, mode, ingredients);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.body() != null && response.body().size() != 0) {
                    Call<Recipe> detailsCall = service.getRecipeById(response.body().get(0).getId(), BuildConfig.API_KEY);
                    detailsCall.enqueue(callback);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Recipes search failed: " + t);
            }
        });
    }

    public void reloadRecipe(Long id, Callback<Recipe> callback) {
        Call<Recipe> recipeById = service.getRecipeById(id, BuildConfig.API_KEY);
        recipeById.enqueue(callback);
    }


    public enum DataSource {
        LOCAL_SQL_DB,
        API_CALL,
        PARSE_DB
    }
}
