package com.example.recipeapp.Models.Parse;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public interface ReviewBuilder {
    void  setBody(String body);
    void  setAuthor(ParseUser user);
    void  setMedia(ParseFile file);
    void  setReviewTo(Long reviewTo);
}
