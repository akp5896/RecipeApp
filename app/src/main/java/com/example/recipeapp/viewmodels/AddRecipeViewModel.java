package com.example.recipeapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.ParseRecipeData;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeViewModel extends ViewModel {
    private static final String TAG = "Add recipe: ";
    private String title;
    private String summary;
    private String time;
    private String servings;
    private String currentIngredient;
    private String currentStep;
    List<String> ingredients = new ArrayList<>();
    public MutableLiveData<String> addedIngredient = new MutableLiveData<>();
    List<Step> steps = new ArrayList<>();
    public MutableLiveData<Step> addedStep = new MutableLiveData<>();

    public MutableLiveData<Boolean> savingResult = new MutableLiveData<>();

    public void saveRecipe() {
        ParseRecipeData parseRecipeData = new ParseRecipeData();
        parseRecipeData.setTitle(title);
        parseRecipeData.setSummary(summary);
        parseRecipeData.setTime(Integer.parseInt(time));
        parseRecipeData.setServings(Integer.parseInt(servings));
        parseRecipeData.setIngredients(ingredients);
        parseRecipeData.setSteps(steps);
        parseRecipeData.setAuthor(ParseUser.getCurrentUser().getUsername());
        parseRecipeData.saveInBackground(e -> {
            if(e != null) {
                Log.w(TAG, "Something went wrong: " + e);
                savingResult.postValue(false);
                return;
            }
            savingResult.postValue(true);
        });
    }

    public void addStep() {
        Step step = new Step(steps.size() + 1, currentStep);
        steps.add(step);
        addedStep.postValue(step);
    }

    public void addIngredient() {
        ingredients.add(currentIngredient);
        addedIngredient.postValue(currentIngredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public MutableLiveData<Step> getAddedStep() {
        return addedStep;
    }

    public void setAddedStep(MutableLiveData<Step> addedStep) {
        this.addedStep = addedStep;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCurrentIngredient() {
        return currentIngredient;
    }

    public void setCurrentIngredient(String currentIngredient) {
        this.currentIngredient = currentIngredient;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}
