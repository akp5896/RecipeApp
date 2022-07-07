package com.example.recipeapp.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.ReviewsAdapter;
import com.example.recipeapp.Models.Parse.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    public MutableLiveData<Boolean> takePicture = new MutableLiveData<>();
    public MutableLiveData<String> reviewSaved = new MutableLiveData<>();
    private MutableLiveData<List<ReviewItemViewModel>> data = new MutableLiveData<>(new ArrayList<>());

    public ReviewsViewModel(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public MutableLiveData<List<ReviewItemViewModel>> getData() {
        loadData();
        return data;
    }

    private void loadData() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_AUTHOR);
        query.addDescendingOrder("createdAt");
        try {
            List<Review> reviews = query.find();
            List<ReviewItemViewModel> viewData = new ArrayList<>();
            for (Review item : reviews) {
                viewData.add(new ReviewItemViewModel(item.getAuthor().getUsername(), item.getBody(), item.getMedia()));
            }
            data.setValue(viewData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        Review review = new Review();
        review.setBody(body);
        review.setAuthor(ParseUser.getCurrentUser());
        review.setMedia(bitmapToParseFile(selectedImage));
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
        adapter.addAtTheBeginning(new ReviewItemViewModel(review.getAuthor().getUsername(), review.getBody(), review.getMedia()));
    }



    @androidx.databinding.BindingAdapter("itemViewModels")
    public static void bindItemViewModels(RecyclerView recyclerView, List<ReviewItemViewModel> data) {
        adapter = getOrCreateAdapter(recyclerView);
        adapter.updateItems(data);
    }

    private static ReviewsAdapter getOrCreateAdapter(RecyclerView recyclerView) {
        if(recyclerView.getAdapter() != null) {
            return (ReviewsAdapter) recyclerView.getAdapter();
        }
        ReviewsAdapter adapter = new ReviewsAdapter();
        recyclerView.setAdapter(adapter);
        return adapter;
    }
}
