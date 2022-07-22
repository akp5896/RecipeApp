package com.example.recipeapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.customviews.MultipleSpinnerItem;
import com.example.recipeapp.Models.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PreferenceScreenViewModel extends ViewModel {

    public MutableLiveData<List<MultipleSpinnerItem>> cuisinesList = new MutableLiveData<>();
    public MutableLiveData<List<MultipleSpinnerItem>> intolerancesList = new MutableLiveData<>();

    public void loadCuisines(String[] cuisines) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ArrayList<MultipleSpinnerItem> cuisinesCheckboxItems = new ArrayList<>();
            for (String cuisine : cuisines) {
                MultipleSpinnerItem cuisineItem = new MultipleSpinnerItem();
                cuisineItem.setTitle(cuisine);
                cuisineItem.setSelected(Settings.getCuisines().contains(cuisine));
                cuisinesCheckboxItems.add(cuisineItem);
            }
            cuisinesList.postValue(cuisinesCheckboxItems);
        });
    }

    public void loadIntolerances(String[] intolerances) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ArrayList<MultipleSpinnerItem> intoleranceCheckboxItems = new ArrayList<>();
            for (String intolerance : intolerances) {
                MultipleSpinnerItem intoleranceItem = new MultipleSpinnerItem();
                intoleranceItem.setTitle(intolerance);
                intoleranceItem.setSelected(Settings.containsIntolerance(intolerance));
                intoleranceCheckboxItems.add(intoleranceItem);
            }
            intolerancesList.postValue(intoleranceCheckboxItems);
        });
    }
}
