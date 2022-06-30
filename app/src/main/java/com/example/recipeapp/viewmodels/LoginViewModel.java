package com.example.recipeapp.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.MainActivity;
import com.example.recipeapp.SignUpActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

import kotlin.reflect.KClass;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = "Logging in: ";

    private String username;
    private String password;

    public MutableLiveData<ParseException> signInSuccessful;

    public LoginViewModel() {

    }

    public void login() {
        ParseUser.logInInBackground(
                username,
                password,
                (user, e) -> {
                    signInSuccessful.setValue(e);
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
