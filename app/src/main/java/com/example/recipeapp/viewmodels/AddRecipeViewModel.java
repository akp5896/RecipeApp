package com.example.recipeapp.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.Parse.ParseRecipeData;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public MutableLiveData<Boolean> takePicture = new MutableLiveData<>();
    public MutableLiveData<Boolean> savingResult = new MutableLiveData<>();
    Bitmap selectedImage;

    public void saveRecipe() {
        ParseRecipeData parseRecipeData = new ParseRecipeData();
        parseRecipeData.setTitle(title);
        parseRecipeData.setSummary(summary);
        parseRecipeData.setTime(Integer.parseInt(time));
        parseRecipeData.setServings(Integer.parseInt(servings));
        parseRecipeData.setIngredients(ingredients);
        parseRecipeData.setSteps(steps);
        parseRecipeData.setAuthor(ParseUser.getCurrentUser().getUsername());
        parseRecipeData.setMedia(bitmapToParseFile(selectedImage));
        parseRecipeData.saveInBackground(e -> {
            if(e != null) {
                Log.w(TAG, "Something went wrong: " + e);
                savingResult.postValue(false);
                return;
            }
            savingResult.postValue(true);
        });
    }

    public Intent onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
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

    public void takePhoto() {
        takePicture.setValue(true);
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

    public Bitmap loadFromUri(Uri photoUri, Activity loadActivity) {
        Bitmap image = null;
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(loadActivity.getContentResolver(), photoUri);
            image = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Bitmap getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Bitmap selectedImage) {
        this.selectedImage = selectedImage;
    }

    @Nullable
    public static ParseFile bitmapToParseFile(Bitmap image) {
        if(image == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        ParseFile result = new ParseFile("myImage.jpg", bitmapBytes);
        return result;
    }
}
