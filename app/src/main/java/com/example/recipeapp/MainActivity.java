package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
//import androidx.databinding.BindingAdapter;
//import androidx.databinding.DataBindingUtil;
import com.example.recipeapp.Fragments.FeedFragment;
import com.example.recipeapp.Fragments.SearchFragment;
import com.example.recipeapp.databinding.ActivityMainBinding;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.recipeapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    FeedFragment feedFragment = new FeedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());//DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        //binding.setViewModel(new AppViewModel());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, feedFragment).commit();
                        Toast.makeText(MainActivity.this, "Feed", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.search:
                        fragmentManager.beginTransaction().replace(R.id.fragmentPlaceholder, new SearchFragment()).commit();
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.suggest:
                        Toast.makeText(MainActivity.this, "Suggest", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return true;
                }
            }
        });
        binding.bottomNavigation.setSelectedItemId(R.id.search);

    }

    public FeedFragment getFeedFragment() {
        return feedFragment;
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}