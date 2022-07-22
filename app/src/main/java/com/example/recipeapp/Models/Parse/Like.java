package com.example.recipeapp.Models.Parse;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("like")
public class Like extends ParseObject {
    public static final String KEY_LIKE_TO = "likeTo";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TITLE = "title";

    public Long getLikeTo() {
        return getLong(KEY_LIKE_TO);
    }

    public void setLikeTo(Long id) {
        put(KEY_LIKE_TO, id);
    }

    public void setLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }
}