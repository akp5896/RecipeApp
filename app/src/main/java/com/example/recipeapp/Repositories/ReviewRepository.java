package com.example.recipeapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.Models.Parse.Review;
import com.example.recipeapp.viewmodels.ReviewItemViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
    private static final String TAG = "ReviewRepository";
    private static ReviewRepository repository;

    public static ReviewRepository getReviewRepository() {
        if(repository == null) {
            repository = new ReviewRepository();
        }
        return repository;
    }

    public MutableLiveData<List<ReviewItemViewModel>> getReviews(Long reviewTo) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_AUTHOR);
        query.whereEqualTo(Review.KEY_REVIEW_TO, reviewTo);
        MutableLiveData<List<ReviewItemViewModel>> data = new MutableLiveData<>(new ArrayList<>());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Cannot find reviews: " + e);
                    return;
                }
                List<ReviewItemViewModel> viewData = new ArrayList<>();
                for (Review item : reviews) {
                    viewData.add(new ReviewItemViewModel(item.getAuthor().getUsername(), item.getBody(), item.getMedia()));
                }
                data.postValue(viewData);
            }
        });
        return data;
    }
}
