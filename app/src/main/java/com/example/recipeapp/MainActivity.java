package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
//import androidx.databinding.BindingAdapter;
//import androidx.databinding.DataBindingUtil;
import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.SearchFragment;
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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    // 5 minutes
    private static final long FIRST_ALARM_TRIGGER = 5 * 60 * 1000;
    // 20 minutes
    private static final long ALARM_INTERVAL = 20 * 60 * 1000;
    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    FeedFragment feedFragment = new FeedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feedFragment).commit();
                        return true;
                    case R.id.search:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, new SearchFragment()).commit();
                        return true;
                    case R.id.suggest:
                        return true;
                    default:
                        return true;
                }
            }
        });
        Settings.getSavedSettings(this);
        Intent i = new Intent(MainActivity.this, EditPreferencesActivity.class);
        startActivity(i);
        setNotifications();
        binding.bottomNavigation.setSelectedItemId(R.id.search);
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
        return true;
    }

    public FeedFragment getFeedFragment() {
        return feedFragment;
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}