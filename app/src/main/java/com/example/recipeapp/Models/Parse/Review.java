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
    public static final String KEY_REVIEW_TO = "reviewTo";

    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }

    public void setMedia(ParseFile file) {
        if(file == null) {
            return;
        }
        put(KEY_MEDIA, file);
    }

    public String getBody() {
        return getString(KEY_BODY);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public ParseFile getMedia() {
        return getParseFile(KEY_MEDIA);
    }

    public void setReviewTo(Long id) {
         put(KEY_REVIEW_TO, id);
    }

    public Long getReviewTo() {
        return getLong(KEY_REVIEW_TO);
    }
}
