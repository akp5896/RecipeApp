package com.example.recipeapp.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.ReviewsAdapter;
import com.example.recipeapp.Models.Parse.RecipeReviewBuilder;
import com.example.recipeapp.Models.Parse.Review;
import com.example.recipeapp.Models.Parse.ReviewBuilder;
import com.example.recipeapp.Repositories.ReviewRepository;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel extends ViewModel {

    private static final String TAG = "ReviewsViewModel";
    private static final String PHOTO_FILE_NAME = "photo.jpg";
    private String title = "";
    private String body = "";
    private static ReviewsAdapter adapter;
    Bitmap selectedImage;
    private Long reviewTo;

    public MutableLiveData<Boolean> takePicture = new MutableLiveData<>();
    public MutableLiveData<String> reviewSaved = new MutableLiveData<>();
    private MutableLiveData<List<ReviewItemViewModel>> data;
    public final MutableLiveData<ReviewItemViewModel> post = new MutableLiveData<>();

    public MutableLiveData<List<ReviewItemViewModel>> getData() {
        data = ReviewRepository.getReviewRepository().getReviews(reviewTo);
        return data;
    }

    public void takePhoto() {
        takePicture.setValue(true);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setReviewTo(Long reviewTo) {
        this.reviewTo = reviewTo;
    }

    public Intent onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public Bitmap loadFromUri(Uri photoUri, Activity loadActivity) {
        Bitmap image = null;
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(loadActivity.getContentResolver(), photoUri);
            image = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Bitmap getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Bitmap selectedImage) {
        this.selectedImage = selectedImage;
    }

    @Nullable
    public static ParseFile bitmapToParseFile(Bitmap image) {
        if(image == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        ParseFile result = new ParseFile("myImage.jpg", bitmapBytes);
        return result;
    }

    public void post() {
        RecipeReviewBuilder recipeReviewBuilder = new RecipeReviewBuilder();
        recipeReviewBuilder.build(body, ParseUser.getCurrentUser(), bitmapToParseFile(selectedImage), reviewTo);
        Review review = recipeReviewBuilder.getReview();
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "Saved");
                if(e != null) {
                    reviewSaved.setValue(e.getMessage());
                    return;
                }
                reviewSaved.setValue("Save successfully");
            }
        });
        post.postValue(new ReviewItemViewModel(review.getAuthor().getUsername(), review.getBody(), review.getMedia()));
    }
}
