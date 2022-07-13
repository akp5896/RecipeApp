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

@AutoValue
public abstract class Recipe implements Parcelable {
    private static final String TAG = "TASTE";
    private static final double INVALID_RATING = -1;
    @SerializedName("title")
    public abstract String getTitle();
    @SerializedName("image")
    public abstract String getImage();
    @SerializedName("id")
    public abstract Long id();
    @SerializedName("readyInMinutes")
    public abstract Integer getReadyInMinutes();
    @SerializedName("healthScore")
    public abstract Double getHealthScore();
    @SerializedName("pricePerServing")
    public abstract Double getPricePerServing();
    @SerializedName("servings")
    public abstract Integer getServings();
    @SerializedName("analyzedInstructions")
    @ParcelAdapter(AnalyzedInstructionsAdapter.class)
    @Nullable
    public abstract List<InstructionEnvelope<List<Step>>> analyzedInstructions();
    @SerializedName("extendedIngredients")
    @Nullable
    public abstract List<Ingredient> ingredients();
    @SerializedName("cuisines")
    public abstract List<String> getCuisines();
    @SerializedName("diets")
    public abstract List<String> getDiets();
    @SerializedName("summary")
    public abstract String getSummary();

    /**
     * Set to an invalid value to make debugging easier
     */
    @Transient
    double userRating = INVALID_RATING;

    public void getTaste(Preferences current) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Taste> call = service.getTasteById(id(), BuildConfig.API_KEY);
        try {
            Response<Taste> response = call.execute();
            setUserRating(Recommendation.getRecipeDistance(Recipe.this, response.body(), current));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTaste(Callback<Taste> callback) {
        RecipeApi service = RetrofitClientInstance.getRetrofitInstance().create(RecipeApi.class);
        Call<Taste> call = service.getTasteById(id(), BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public static TypeAdapter<Recipe> typeAdapter(Gson gson) {
        return new AutoValue_Recipe.GsonTypeAdapter(gson);
    }

    public static class AnalyzedInstructionsAdapter implements com.ryanharter.auto.value.parcel.TypeAdapter<List<InstructionEnvelope<List<Step>>>> {

        @Override
        public List<InstructionEnvelope<List<Step>>> fromParcel(Parcel in) {
            List<Step> steps = new ArrayList<>();
            in.readList(steps, Step.class.getClassLoader());
            InstructionEnvelope<List<Step>> envelope = new InstructionEnvelope<>();
            envelope.results = steps;
            return new ArrayList<InstructionEnvelope<List<Step>>>(){{add(envelope);}};
        }

        @Override
        public void toParcel(List<InstructionEnvelope<List<Step>>> value, Parcel dest) {
            dest.writeList(value.get(0).results);
        }
    }
}
