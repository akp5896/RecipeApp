package com.example.recipeapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeViewModel extends ViewModel {
    private String title;
    private String summary;
    private String time;
    private String servings;
    private String currentIngredient;
    private String currentStep;
    private String cuisine;
    List<String> ingredients = new ArrayList<>();
    public MutableLiveData<String> addedIngredient = new MutableLiveData<>();
    List<Step> steps = new ArrayList<>();
    public MutableLiveData<Step> addedStep = new MutableLiveData<>();

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

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}
