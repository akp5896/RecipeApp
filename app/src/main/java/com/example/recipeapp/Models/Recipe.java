package com.example.recipeapp.Models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recipeapp.BuildConfig;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Retrofit.InstructionEnvelope;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Utils.Recommendation;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Entity
@Parcel
public class Recipe {
    private static final String TAG = "TASTE";
    private static final double INVALID_RATING = -1;
    @ColumnInfo
    @SerializedName("title")
    public String title;
    @ColumnInfo
    @SerializedName("image")
    public String image;
    @ColumnInfo
    @PrimaryKey
    @SerializedName("id")
    public Long id;
    @ColumnInfo
    @SerializedName("readyInMinutes")
    public Integer readyInMinutes;
    @ColumnInfo
    @SerializedName("healthScore")
    public Double healthScore;
    @ColumnInfo
    @SerializedName("pricePerServing")
    public Double pricePerServing;
    @ColumnInfo
    @SerializedName("servings")
    public Integer servings;
    @ColumnInfo
//    @SerializedName("analyzedInstructions")
    public List<Step> analyzedInstructions;
    @ColumnInfo
    @SerializedName("extendedIngredients")
    public List<Ingredient> ingredients;
    @ColumnInfo
    @SerializedName("cuisines")
    public List<String> cuisines;
    @ColumnInfo
    @SerializedName("diets")
    public List<String> diets;
    public boolean isBookmarked = false;
    @SerializedName("summary")
    public String summary;

    /**
     * Set to an invalid value to make debugging easier
     */
    @Transient
    double userRating = INVALID_RATING;

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    public List<String> getCuisines() {
        return cuisines;
    }

    public List<String> getDiets() {
        return diets;
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

    public void setAnalyzedInstructions(List<Step> analyzedInstructions) {
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

    public List<Step> getAnalyzedInstructions() {
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

    public void getTaste(Preferences current) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Taste> call = service.getTasteById(id, BuildConfig.API_KEY);
        try {
            Response<Taste> response = call.execute();
            setUserRating(Recommendation.getRecipeDistance(Recipe.this, response.body(), current));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getSummary() {
        return summary;
    }

}
