package com.example.recipeapp.viewmodels;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.DetailsActivity;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.ParseApplication;
import com.example.recipeapp.R;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Room.RecipeDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    public MutableLiveData<List<Ingredient>> showIngredients = new MutableLiveData<>();
    public MutableLiveData<Integer> bookmarkToast = new MutableLiveData<>();

    private Recipe recipe;

    private final String servings;
    private final String time;
    private final String image;

    public DetailsViewModel(Recipe recipe) {
        this.recipe = recipe;
        servings = recipe.servings.toString();
        time = recipe.readyInMinutes.toString();
        image = recipe.getImage();

        if(recipe.getAnalyzedInstructions() == null || recipe.getIngredients() == null) {
            RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
            Call<Recipe> recipeById = service.getRecipeById(recipe.getId(), BuildConfig.API_KEY);
            recipeById.enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    recipe.setAnalyzedInstructions(response.body().getAnalyzedInstructions());
                    recipe.setIngredients(response.body().getIngredients());
                    setDetails();
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    Log.e(TAG, "Something went wrong: " + t);
                }
            });
        } else {
            setDetails();
        }
    }

    private void setDetails() {
//        for (Step item : recipe.getAnalyzedInstructions()) {
//            steps.add(item.number + ". " + item.step);
//        }
//        stepsAdapter.notifyItemRangeChanged(0, steps.size());
    }

    public void showIngredients() {
        showIngredients.setValue(recipe.getIngredients());
    }

    public void bookmark() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            RecipeDatabase recipeDatabase = RecipeDatabase.getRecipeDatabase();
            if(recipe.isBookmarked == null) {
                recipeDatabase.runInTransaction(() -> {
                    recipe.isBookmarked = (recipeDatabase.recipeDao().getRecipeById(recipe.id) > 0);
                    changeBookmark(handler, recipeDatabase);
                });
            }
            else {
                changeBookmark(handler, recipeDatabase);
            }
        });
    }

    private void changeBookmark(Handler handler, RecipeDatabase recipeDatabase) {
        recipe.isBookmarked = !recipe.isBookmarked;
        if(recipe.isBookmarked) {
            recipeDatabase.runInTransaction(() -> recipeDatabase.recipeDao().insertRecipe(recipe));
            handler.post(() -> bookmarkToast.setValue(R.string.recipe_bookmarked));
        }
        else {
            recipeDatabase.runInTransaction(() -> recipeDatabase.recipeDao().delete(recipe));
            handler.post(() -> bookmarkToast.setValue(R.string.recipe_unbookmarked));
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getServings() {
        return String.format("%s\nservings", servings);
    }

    public String getTime() {
        return String.format("%s minutes", time);
    }

    public String getImage() {
        return image;
    }

    @androidx.databinding.BindingAdapter("recipePhoto")
    public static void bindItemViewModels(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).into(imageView);
    }
}
