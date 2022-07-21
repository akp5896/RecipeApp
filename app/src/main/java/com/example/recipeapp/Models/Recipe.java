package com.example.recipeapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Retrofit.InstructionEnvelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Utils.Recommendation;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.parse.ParseUser;
import com.ryanharter.auto.value.parcel.ParcelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Transient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.sweers.autotransient.AutoTransient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@org.parceler.Parcel
public class Recipe {
    private static final String TAG = "TASTE";
    private static final double INVALID_RATING = -1;
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
    @SerializedName("analyzedInstructions")
    @Nullable
    List<Step> analyzedInstructions;
    @SerializedName("extendedIngredients")
    @Nullable
    List<Ingredient> ingredients;
    @SerializedName("cuisines")
    List<String> cuisines;
    @SerializedName("diets")
    List<String> diets;
    @SerializedName("summary")
    public String summary;

    /**
     * Set to an invalid value to make debugging easier
     */
    @Transient
    double userRating = INVALID_RATING;
    public Recipe(){}
    public Recipe(String title, String image, Long id, Integer readyInMinutes, Double healthScore, Double pricePerServing, Integer servings, @Nullable List<Step> analyzedInstructions, @Nullable List<Ingredient> ingredients, List<String> cuisines, List<String> diets, String summary) {
        this.title = title;
        this.image = image;
        this.id = id;
        this.readyInMinutes = readyInMinutes;
        this.healthScore = healthScore;
        this.pricePerServing = pricePerServing;
        this.servings = servings;
        this.analyzedInstructions = analyzedInstructions;
        this.ingredients = ingredients;
        this.cuisines = cuisines;
        this.diets = diets;
        this.summary = summary;
        this.userRating = userRating;
    }

    public void getTaste(Callback<Taste> callback) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Taste> call = service.getTasteById(id, BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public Double getPricePerServing() {
        return pricePerServing;
    }

    public void setPricePerServing(Double pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    @Nullable
    public List<Step> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(@Nullable List<Step> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    @Nullable
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(@Nullable List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
