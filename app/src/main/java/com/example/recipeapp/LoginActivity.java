package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.recipeapp.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN ACTIVITY";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(ParseUser.getCurrentUser() != null) {
            toMainActivity();
        }


        binding.btnLogin.setOnClickListener(v -> login());

        binding.tvSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    private void login() {
        ParseUser.logInInBackground(
                binding.etLogin.getText().toString(),
                binding.etPass.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e != null) {
                            Log.i(TAG, "Issue with login" + e);
                            return;
                        }
                        toMainActivity();
                    }
                });
    }

    private void toMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}