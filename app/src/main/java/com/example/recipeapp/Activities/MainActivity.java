package com.example.recipeapp.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.SearchFragment;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.NotificationAlarmReceiver;
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