package com.example.recipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.recipeapp.EditPreferencesActivity;
import com.example.recipeapp.Fragments.BookmarkFeedFragment;
import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.RecommendListFragment;
import com.example.recipeapp.Fragments.SearchFragment;
import com.example.recipeapp.Fragments.SuggestFragment;
import com.example.recipeapp.Models.Parse.Like;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.Settings;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Utils.NotificationAlarmReceiver;
import com.example.recipeapp.Utils.RecommendCallback;
import com.example.recipeapp.Utils.Recommendation;
import com.example.recipeapp.databinding.ActivityMainBinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.example.recipeapp.Utils.GalleryHandler;
import com.example.recipeapp.Utils.ProfileSetup;
import com.example.recipeapp.databinding.ProfileLayoutBinding;

import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.SearchFragment;
import com.example.recipeapp.Fragments.UserFeedFragment;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.NotificationAlarmReceiver;
import com.example.recipeapp.Utils.ShareRecipe;
import com.example.recipeapp.databinding.ActivityMainBinding;
import com.example.recipeapp.viewmodels.LoginViewModel;
import com.example.recipeapp.viewmodels.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    // 2 hours
    private static final long FIRST_ALARM_TRIGGER = 2 * 60 * 60 * 1000;
    // 10 hours
    private static final long ALARM_INTERVAL = 10 * 60 * 60 * 1000;
    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    UserFeedFragment userFeedFragment = new UserFeedFragment();
    SearchFragment searchFragment = new SearchFragment();
    SuggestFragment suggestFragment = new SuggestFragment();
    BookmarkFeedFragment feedFragment = new BookmarkFeedFragment();
    MainViewModel viewModel;
    public FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, userFeedFragment).commit();
                        return true;
                    case R.id.search:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, searchFragment).commit();
                        return true;
                    case R.id.suggest:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, suggestFragment).commit();
                        return true;
                    default:
                        return true;
                }
            }
        });
        Settings.getSavedSettings(this);

        // Should create a launcher in onCreate, couldn't do it in a later called callback
        ActivityResultLauncher<Intent> launcher = GalleryHandler.getGalleryLauncher(ProfileSetup.getHeaderCallback(binding.drawer, this), this);
        binding.drawer.ivProfilePic.setOnClickListener(v -> launcher.launch(Intent.createChooser(GalleryHandler.onPickPhoto(), "Select Picture")));
        binding.drawer.ivPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditPreferencesActivity.class));
            }
        });
        ProfileSetup.initialize(binding.drawer, this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        )
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                    37);

        }
        //ShareRecipe.startDiscovery(this, ParseUser.getCurrentUser().getUsername());
        setNotifications();
        binding.bottomNavigation.setSelectedItemId(R.id.search);

        binding.drawer.fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public ProfileLayoutBinding getProfileLayoutBinding() {
        return binding.drawer;
    }

    private void setNotifications() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, FIRST_ALARM_TRIGGER, ALARM_INTERVAL, alarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.i(TAG, "Logout failed");
                        return;
                    }
                    finish();
                }
            });
        }
        if(item.getItemId() == R.id.accept_recipe) {
            Toast.makeText(this, "Now ready to accept shared recipes", Toast.LENGTH_LONG).show();
            ShareRecipe.startDiscovery(this, ParseUser.getCurrentUser().getUsername());
        }
        if(item.getItemId() == R.id.getNearby) {
            getLocation(viewModel.getLikesInLocationListener());
            viewModel.recipesNearby.observe(this, new Observer<List<Pair<String, Long>>>() {
                @Override
                public void onChanged(List<Pair<String, Long>> recipes) {
                    RecommendListFragment.newInstance(recipes).show(fragmentManager, "");
                }
            });
        }
        if(item.getItemId() == R.id.bookmark) {
            fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feedFragment).commit();
        }
        return true;
    }

    private void getLocation(OnSuccessListener<? super Location> onSuccess) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    37);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, onSuccess)
                .addOnFailureListener(e -> Log.e(TAG, "error" + e));
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}