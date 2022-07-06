package com.example.recipeapp.Models.Parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Reviews")
public class Review extends ParseObject {
    public static final String KEY_BODY = "body";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_MEDIA = "media";

    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }

    public void setMedia(ParseFile file) {
        put(KEY_MEDIA, file);
    }
}
