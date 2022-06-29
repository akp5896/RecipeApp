package com.example.recipeapp.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;

import com.example.recipeapp.MainActivity;
import com.example.recipeapp.SignUpActivity;
import com.parse.ParseUser;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = "Logging in: ";

    private String username;
    private String password;

    public LoginViewModel() {

    }

    public void login(View view) {
        ParseUser.logInInBackground(
                username,
                password,
                (user, e) -> {
                    if(e != null) {
                        Log.i(TAG, "Issue with login" + e);
                        return;
                    }
                    Context context = view.getContext();
                    context.startActivity(new Intent(context, MainActivity.class));
                });
    }

    public void signUp(View view) {
        Context context = view.getContext();
        context.startActivity(new Intent(context, SignUpActivity.class));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
