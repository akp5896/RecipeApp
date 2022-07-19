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
import com.example.recipeapp.Models.Parse.ParseRecipe;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    public MutableLiveData<List<Ingredient>> showIngredients = new MutableLiveData<>();
    private MutableLiveData<List<StepViewModel>> steps = new MutableLiveData<>();
    public MutableLiveData<Integer> numberOfLikes;
    RecipesRepository repo = RecipesRepository.getRepository();

    private Recipe recipe;
    private final String servings;
    private final String time;
    private final String image;

    public DetailsViewModel(Recipe recipe) {
        this.recipe = recipe;
        servings = recipe.getServings().toString();
        time = recipe.getReadyInMinutes().toString();
        image = recipe.getImage();
        steps.setValue(new ArrayList<>());
        setDetails();
        if(recipe.getIngredients() == null) {
            repo.reloadRecipe(recipe.getId());
        }
        numberOfLikes = RecipesRepository.getRepository().getLikes(recipe.getId());
    }

    public MutableLiveData<List<StepViewModel>> getSteps() {
        return steps;
    }

    private void setDetails() {
        List<StepViewModel> stepViewModels = new ArrayList<>();
        if(recipe.getAnalyzedInstructions() == null) {
            Log.w(TAG, "Failed to load instructions");
            return;
        }
        for(Step item : recipe.getAnalyzedInstructions().get(0).results) {
            stepViewModels.add(new StepViewModel(item.getStep(), item.getNumber()));
        }
        steps.setValue(stepViewModels);
    }

    public void showIngredients() {
        showIngredients.setValue(recipe.getIngredients());
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
