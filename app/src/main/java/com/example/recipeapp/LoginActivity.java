package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.recipeapp.viewmodels.LoginViewModel;
import com.parse.ParseUser;
import com.example.recipeapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN ACTIVITY";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(ViewModelProviders.of(this).get(LoginViewModel.class));

        if(ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        binding.getViewModel().isLogInSuccessful.observe(this, errorCode -> {
            Context context = getApplicationContext();
            switch (errorCode) {
                case SUCCESS:
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                    break;
                case PARSE_ERROR:
                case PARSE_INVALID_CREDENTIALS:
                default:
                    Toast.makeText(context, "Unable to login: " + errorCode.message, Toast.LENGTH_SHORT).show();
            }
        });

        binding.getViewModel().startSignUp.observe(this, startSignUp -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }
}