package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.Activities.LoginActivity;
import com.example.recipeapp.Models.Parse.LiveUserData;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserRepository {

    private static final String TAG = "User repository";
    private static final String KEY_USERNAME = "username";
    public static final String USER_BIO = "bio";
    private static final String USER_PROFILE_PICTURE = "profilePicture";
    private static UserRepository repository;

    public static UserRepository getRepository() {
        if(repository == null) {
            repository = new UserRepository();
        }
        return repository;
    }

    public LiveUserData getUserByUsername(String username) {
        LiveUserData liveUserData = new LiveUserData(new MutableLiveData<String>(), new MutableLiveData<ParseFile>());
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo(KEY_USERNAME, username);
        query.findInBackground((users, e) -> {
            if(e != null) {
                Log.w(TAG, "cannot find username");
                return;
            }
            ParseUser user = users.get(0);
            user.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    liveUserData.getBio().postValue(user.getString(USER_BIO));
                    liveUserData.getImage().postValue(user.getParseFile(USER_PROFILE_PICTURE));
                }
            });
        });
        return liveUserData;
    }
}
