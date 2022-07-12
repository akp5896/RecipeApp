package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.databinding.ActivitySignUpBinding;
import com.example.recipeapp.viewmodels.SignUpViewModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SIGN UP ACTIVITY";
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setViewModel(new SignUpViewModel());

        binding.getViewModel().signUpResult.observe(this, signUpResult -> {
            switch (signUpResult) {
                case SUCCESS:
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                case ERROR_CREATING_PREFERENCES:
                case ERROR_CREATING_TASTE:
                case ERROR_CREATING_USER:
                    Toast.makeText(SignUpActivity.this, signUpResult.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}