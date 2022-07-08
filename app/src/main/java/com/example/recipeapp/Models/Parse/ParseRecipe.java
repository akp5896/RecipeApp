package com.example.recipeapp.Models.Parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Recipe")
public class ParseRecipe extends ParseObject {
    public static final String KEY_NUM_OF_LIKES = "numOfLikes";
    public static final String KEY_LIKED_BY = "likedBy";
    public static final String KEY_ID = "recipeId";
    private static final String TAG = "Parse recipe";

    public int getNumberOfLiked() {
        return getInt(KEY_NUM_OF_LIKES);
    }

    public void like() {
        isLiked((likedUsers, e) -> {
            if(likedUsers.size() > 0) {
                increment(KEY_NUM_OF_LIKES, -1);
                getRelation(KEY_LIKED_BY).remove(ParseUser.getCurrentUser());
            }
            else {
                increment(KEY_NUM_OF_LIKES);
                getRelation(KEY_LIKED_BY).add(ParseUser.getCurrentUser());
            }
            saveInBackground();
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
        findById(id, (objects, e) -> {
            if(objects == null || objects.size() == 0) {
                ParseRecipe parseRecipe = new ParseRecipe();
                parseRecipe.setId(id);
                parseRecipe.saveInBackground(e1 -> {
                    if(e1 != null) {
                        Log.e(TAG, "Not found, cannot create" + e);
                        return;
                    }
                    parseRecipe.like();
                });
                return;
            }
            objects.get(0).like();
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
