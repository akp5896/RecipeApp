package com.example.recipeapp.viewmodels;

import android.util.Log;
import android.widget.ImageView;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Adapters.StepsAdapter;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.RecipeWidget;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    public MutableLiveData<List<Ingredient>> showIngredients = new MutableLiveData<>();
    public MutableLiveData<Integer> bookmarkToast = new MutableLiveData<>();
    public MutableLiveData<List<StepViewModel>> steps = new MutableLiveData<>();
    public MutableLiveData<Boolean> liked = new MutableLiveData<>();
    public MutableLiveData<String> widgetLoaded = new MutableLiveData<>();
    RecipesRepository repo = RecipesRepository.getRepository();
    public MutableLiveData<Integer> numberOfLikes;
    public MutableLiveData<Boolean> executePendingBindings = new MutableLiveData<>();

    private Recipe recipe;
    private String servings;
    private String time;
    private String image;



    public DetailsViewModel(Recipe passedRecipe) {
        this.recipe = passedRecipe;
        if(passedRecipe.getIngredients() == null) {
            repo.reloadRecipe(passedRecipe.getId(), new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    if(response.body() != null) {
                        recipe = response.body();
                    }
                    setDetails();
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    Log.e(TAG, "Something went wrong: " + t);
                }
            });
        }
        else {
            setDetails();
        }
        numberOfLikes = RecipesRepository.getRepository().getLikes(recipe.getId());
    }

    public MutableLiveData<List<StepViewModel>> getSteps() {
        return steps;
    }

    private void setDetails() {
        servings = recipe.getServings().toString();
        time = recipe.getReadyInMinutes().toString();
        image = recipe.getImage();
        steps.setValue(new ArrayList<>());
        executePendingBindings.postValue(true);
        List<StepViewModel> stepViewModels = new ArrayList<>();
        if(recipe.getAnalyzedInstructions() == null) {
            Log.w(TAG, "Failed to load instructions");
            return;
        }
        for(Step item : recipe.getAnalyzedInstructions()) {
            stepViewModels.add(new StepViewModel(item.getStep(), item.getNumber()));
        }
        steps.setValue(stepViewModels);
    }

    public void showIngredients() {showIngredients.setValue(recipe.ingredients);
    }

    public void bookmark() {
        repo.bookmark(recipe, result -> {
            if(result == RecipesRepository.BookmarkCallback.BookmarkResult.BOOKMARKED) {
                bookmarkToast.postValue(R.string.recipe_bookmarked);
            }
            else {
                bookmarkToast.postValue(R.string.recipe_unbookmarked);
            }
        });
    }

    public void onLike() {
        liked.postValue(true);
        RecipesRepository.getRepository().getTaste(recipe.getId(), new Callback<Taste>() {
            @Override
            public void onResponse(Call<Taste> call, Response<Taste> response) {
                try {
                    Preferences preferences = (Preferences) ParseUser.getCurrentUser().fetchIfNeeded().getParseObject(Preferences.PREFERENCES);
                    preferences.updatePreferences(recipe, response.body());
                    preferences.saveInBackground();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Taste> call, Throwable t) {
                Log.e(TAG, "Failure : " + t);
            }
        });
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getServings() {
        return String.format("%s\nservings", recipe.getServings());
    }

    public String getTime() {
        return String.format("%s minutes", recipe.getReadyInMinutes());
    }

    public String getImage() {
        return recipe.getImage();
    }

    @androidx.databinding.BindingAdapter("recipePhoto")
    public static void bindItemViewModels(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).into(imageView);
    }

    public void shareWidget() {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        service.getRecipeWidget(recipe.getId(), BuildConfig.API_KEY).enqueue(new Callback<RecipeWidget<String>>() {
            @Override
            public void onResponse(Call<RecipeWidget<String>> call, Response<RecipeWidget<String>> response) {
                widgetLoaded.setValue(response.body().url);
            }

            @Override
            public void onFailure(Call<RecipeWidget<String>> call, Throwable t) {
                Log.w(TAG, "Cannot get recipe: " + t);
            }
        });
    }
}
