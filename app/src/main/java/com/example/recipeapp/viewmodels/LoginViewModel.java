package com.example.recipeapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "Logging in: ";
    private static final int INVALID_LOGIN_PARAMS = 101;
    private String username = "";
    private String password = "";


    public MutableLiveData<Boolean> startSignUp = new MutableLiveData<>();
    public MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LoginViewModel() {
    }

    public void login() {
        ParseUser.logInInBackground(
                username,
                password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e != null) {
                            Log.i(TAG, "Issue with login" + e);
                            switch (e.getCode()) {
                                case INVALID_LOGIN_PARAMS:
                                case ParseException.USERNAME_MISSING:
                                case ParseException.PASSWORD_MISSING:
                                    loginResult.setValue(LoginResult.PARSE_INVALID_CREDENTIALS);
                                    break;
                                default:
                                    loginResult.setValue(LoginResult.PARSE_ERROR);
                            }
                            return;
                        }
                        loginResult.setValue(LoginResult.SUCCESS);
                    }
                });
    }

    public void signUp() {
        startSignUp.setValue(true);
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

    public enum LoginResult {
        SUCCESS("Logged in successfully"),
        PARSE_ERROR("Unknown error"),
        PARSE_INVALID_CREDENTIALS("Invalid username/password");

        public final String message;

        LoginResult(String message) {
            this.message = message;
        }
    }
}
