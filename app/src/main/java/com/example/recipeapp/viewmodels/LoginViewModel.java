package com.example.recipeapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {

    private String username;
    private String password;


    public MutableLiveData<Boolean> startSignUp = new MutableLiveData<>();
    public MutableLiveData<ParseException> isLogInSuccessful = new MutableLiveData<>();

    public LoginViewModel() {
    }

    public void login() {
        ParseUser.logInInBackground(
                username,
                password,
                (user, e) -> isLogInSuccessful.setValue(e));
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
}
