package com.example.recipeapp.Models;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Retrofit.InstructionEnvelope;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Recipe {
    @SerializedName("title")
    String title;
    @SerializedName("image")
    String image;
    @SerializedName("id")
    Long id;
    @SerializedName("readyInMinutes")
    Integer readyInMinutes;
    @SerializedName("healthScore")
    Double healthScore;
    @SerializedName("pricePerServing")
    Double pricePerServing;
    @SerializedName("servings")
    Integer servings;
    //List<String> analyzedInstructions;
    @SerializedName("analyzedInstructions")
    @Transient
    List<InstructionEnvelope<List<Step>>> analyzedInstructions;
    @SerializedName("extendedIngredients")
    List<Ingredient> ingredients;
    @SerializedName("cuisines")
    List<String> cuisines;
    @SerializedName("diets")
    List<String> diets;

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setAnalyzedInstructions(List<InstructionEnvelope<List<Step>>> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public Long getId() {
        return id;
    }

    public Integer getTimeToCook() {
        return readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public List<InstructionEnvelope<List<Step>>> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public Double getPricePerServing() {
        return pricePerServing;
    }
}
