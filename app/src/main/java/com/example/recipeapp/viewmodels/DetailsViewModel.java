package com.example.recipeapp.viewmodels;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Repositories.RecipesRepository;
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
    private MutableLiveData<List<StepViewModel>> steps = new MutableLiveData<>();
    public MutableLiveData<Boolean> liked = new MutableLiveData<>();
    RecipesRepository repo = RecipesRepository.getRepository();

    private Recipe recipe;

    public DetailsViewModel(Recipe passedRecipe) {
        this.recipe = passedRecipe;
        steps.setValue(new ArrayList<>());
        setDetails();
        if(recipe.getIngredients() == null) {
            repo.reloadRecipe(recipe.getId());
        }
    }

    public MutableLiveData<List<StepViewModel>> getSteps() {
        return steps;
    }

    private void setDetails() {
        List<StepViewModel> stepViewModels = new ArrayList<>();
        if(recipe.analyzedInstructions == null) {
            Log.w(TAG, "Failed to load instructions");
            return;
        }
        for(Step item : recipe.analyzedInstructions) {
            stepViewModels.add(new StepViewModel(item.getStep(), item.getNumber()));
        }
        steps.setValue(stepViewModels);
    }

    public void showIngredients() {
        showIngredients.setValue(recipe.ingredients);
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
        recipe.getTaste(new Callback<Taste>() {
            @Override
            public void onResponse(Call<Taste> call, Response<Taste> response) {
                Preferences preferences = (Preferences) ParseUser.getCurrentUser().getParseObject(Preferences.PREFERENCES);
                preferences.updatePreferences(recipe, response.body());
                preferences.saveInBackground();
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
        return recipe.getImage;
    }

    @androidx.databinding.BindingAdapter("recipePhoto")
    public static void bindItemViewModels(ImageView imageView, String image) {
        Glide.with(imageView.getContext()).load(image).into(imageView);
    }

    @androidx.databinding.BindingAdapter("stepsViewModel")
    public static void bindItemViewModels(RecyclerView recyclerView, List<StepViewModel> data) {
        StepsAdapter adapter = getOrCreateAdapter(recyclerView);
        adapter.updateItems(data);
    }

    private static StepsAdapter getOrCreateAdapter(RecyclerView recyclerView) {
        if(recyclerView.getAdapter() != null) {
            return (StepsAdapter) recyclerView.getAdapter();
        }
        StepsAdapter adapter = new StepsAdapter();
        recyclerView.setAdapter(adapter);
        return adapter;
    }
}
