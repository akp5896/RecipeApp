package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.recipeapp.viewmodels.LoginViewModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.recipeapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN ACTIVITY";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(new LoginViewModel());

        if(ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        binding.getViewModel().signInSuccessful.observe(this, e -> {
            Context context = getApplicationContext();
            if(e != null) {
                Log.i(TAG, "Issue with login" + e);
                Toast.makeText(context, "Unable to login", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(context, MainActivity.class));
        });
    }
}