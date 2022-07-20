package com.example.recipeapp.Activities;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.recipeapp.Utils.GalleryHandler;
import com.example.recipeapp.Utils.ProfileSetup;
import com.example.recipeapp.databinding.ProfileLayoutBinding;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.SearchFragment;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.NotificationAlarmReceiver;
import com.example.recipeapp.Utils.ShareRecipe;
import com.example.recipeapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

        // Should create a launcher in onCreate, couldn't do it in a later called callback
        ActivityResultLauncher<Intent> launcher = GalleryHandler.getGalleryLauncher(ProfileSetup.getHeaderCallback(binding.drawer, this), this);
        binding.drawer.ivProfilePic.setOnClickListener(v -> launcher.launch(Intent.createChooser(GalleryHandler.onPickPhoto(), "Select Picture")));

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
        return true;
    }

    public FeedFragment getFeedFragment() {
        return feedFragment;
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}