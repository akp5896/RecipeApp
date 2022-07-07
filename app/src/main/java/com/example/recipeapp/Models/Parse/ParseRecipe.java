package com.example.recipeapp.Models.Parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Recipe")
public class ParseRecipe extends ParseObject {
    public static final String KEY_NUM_OF_LIKES = "numOfLikes";
    public static final String KEY_LIKED_BY = "likedBy";
    public static final String KEY_ID = "id";
    private static final String TAG = "Parse recipe";

    public int getNumberOfLiked() {
        return getInt(KEY_NUM_OF_LIKES);
    }

    public void like() {
        isLiked((likedUsers, e) -> {
            if(likedUsers.size() > 0) {
                increment(KEY_LIKED_BY, -1);
                getRelation(KEY_LIKED_BY).remove(ParseUser.getCurrentUser());
            }
            else {
                increment(KEY_NUM_OF_LIKES);
                getRelation(KEY_LIKED_BY).add(ParseUser.getCurrentUser());
            }
        });
    }

    public void isLiked(FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = getRelation(KEY_LIKED_BY).getQuery();
        query.whereEqualTo(ParseObject.KEY_OBJECT_ID, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(callback);
    }

    public void setId(Long id) {
        put(KEY_ID, id);
    }

    public static void likeById(Long id) {
        findById(id, new FindCallback<ParseRecipe>() {
            @Override
            public void done(List<ParseRecipe> objects, ParseException e) {
                if(objects == null || objects.size() == 0) {
                    Log.i(TAG, "Not found");
                    return;
                }
                objects.get(0).like();
            }
        });
    }

    public static void findAllById(List<Long> id, FindCallback<ParseRecipe> callback) {
        ParseQuery<ParseRecipe> query = ParseQuery.getQuery(ParseRecipe.class);
        query.whereContainedIn(KEY_ID, id);
        query.findInBackground(callback);
    }

    public static void findById(Long id, FindCallback<ParseRecipe> callback) {
        ParseQuery<ParseRecipe> query = ParseQuery.getQuery(ParseRecipe.class);
        query.whereEqualTo(KEY_ID, id);
        query.findInBackground(callback);
    }
}
