package com.example.recipeapp.Models.Parse;

import com.parse.ParseFile;
import com.parse.ParseUser;

public class RecipeReviewBuilder implements ReviewBuilder{
    private Review review;

    public RecipeReviewBuilder() {
        this.review = new Review();
    }

    @Override
    public void setBody(String body) {
        review.setBody(body);
    }

    @Override
    public void setAuthor(ParseUser user) {
        review.setAuthor(user);
    }

    @Override
    public void setMedia(ParseFile file) {
        review.setMedia(file);
    }

    @Override
    public void setReviewTo(Long reviewTo) {
        review.setReviewTo(reviewTo);
    }

    public void build(String body, ParseUser user, ParseFile file, Long reviewTo) {
        setBody(body);
        setAuthor(user);
        setMedia(file);
        setReviewTo(reviewTo);
    }

    public Review getReview() {
        return review;
    }
}
