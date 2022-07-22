package com.example.recipeapp.viewmodels;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Fragments.RecommendListFragment;
import com.example.recipeapp.Models.Parse.Like;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    public MutableLiveData<List<Pair<String, Long>>> recipesNearby = new MutableLiveData<>();

    public OnSuccessListener<Location> getLikesInLocationListener() {
        return new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
                parseGeoPoint.setLatitude(location.getLatitude());
                parseGeoPoint.setLongitude(location.getLongitude());
                recipesNearby = RecipesRepository.getRepository().getNearbyRecipes(parseGeoPoint);
            }
        };
    }
}
