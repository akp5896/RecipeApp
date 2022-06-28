package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();

                user.put(USERNAME, binding.etLogin.getText().toString());
                user.put(PASSWORD, binding.etLogin.getText().toString());
                user.signUpInBackground((SignUpCallback) e -> {
                    if(e != null) {
                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.i("SIGN UP ACTIVTY", e.toString());
                        return;
                    }
                    Preferences pref = new Preferences();
                    Taste taste = new Taste();
                    try {
                        taste.save();
                        pref.put(Preferences.KEY_USER_TASTE, taste);
                        pref.saveInBackground(e1 -> {
                            user.put("preferences", pref);
                            user.saveInBackground();
                        });
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                });

            }
        });
    }
}