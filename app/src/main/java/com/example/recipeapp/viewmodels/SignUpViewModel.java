package com.example.recipeapp.viewmodels;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeapp.LoginActivity;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.SignUpActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpViewModel extends ViewModel {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TAG = "Sign up view model: ";

    public MutableLiveData<SignUpResult> signUpResult = new MutableLiveData<>();

    private String username = "";
    private String password = "";

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

    public void signUp() {
        ParseUser user = new ParseUser();
        user.put(USERNAME, username);
        user.put(PASSWORD, password);
        user.signUpInBackground((SignUpCallback) e -> {
            if(e != null) {
                signUpResult.setValue(SignUpResult.ERROR_CREATING_USER);
                Log.i(TAG, e.toString());
                return;
            }
            Preferences pref = new Preferences();
            Taste taste = new Taste();
            try {
                taste.save();
                pref.put(Preferences.KEY_USER_TASTE, taste);
                pref.saveInBackground(e1 -> {
                    user.put(Preferences.PREFERENCES, pref);
                    user.saveInBackground(e2 -> signUpResult.setValue(SignUpResult.ERROR_CREATING_PREFERENCES));
                });
            } catch (ParseException ex) {
                signUpResult.setValue(SignUpResult.ERROR_CREATING_TASTE);
                ex.printStackTrace();
            }
            signUpResult.setValue(SignUpResult.SUCCESS);
        });
    }

    public enum SignUpResult {
        SUCCESS,
        ERROR_CREATING_USER,
        ERROR_CREATING_TASTE,
        ERROR_CREATING_PREFERENCES
    }
}
