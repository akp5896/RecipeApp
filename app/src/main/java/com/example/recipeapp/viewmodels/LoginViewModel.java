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
    public MutableLiveData<LoginResult> isLogInSuccessful = new MutableLiveData<>();

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
                            if(e.getCode() == INVALID_LOGIN_PARAMS
                                    || e.getCode() == ParseException.USERNAME_MISSING
                                    || e.getCode() == ParseException.PASSWORD_MISSING) {
                                isLogInSuccessful.setValue(LoginResult.PARSE_INVALID_CREDENTIALS);
                            }
                            else {
                                isLogInSuccessful.setValue(LoginResult.PARSE_ERROR);
                            }
                            return;
                        }
                        isLogInSuccessful.setValue(LoginResult.SUCCESS);
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
