package com.example.recipeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.recipeapp.databinding.ActivityReviewsBinding;
import com.example.recipeapp.viewmodels.ReviewsViewModel;

import java.io.File;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = "ReviewsActivity";
    ActivityReviewsBinding binding;
    ReviewsViewModel viewModel;
    ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewsBinding.inflate(getLayoutInflater());
        binding.setViewModel(new ReviewsViewModel(getIntent().getStringExtra(DetailsActivity.RECIPE), getString(R.string.write_review)));
        binding.executePendingBindings();
        setContentView(binding.getRoot());
        galleryLauncher = getGalleryLauncher();
        viewModel = binding.getViewModel();

        viewModel.takePicture.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean takePicture) {
                galleryLauncher.launch(Intent.createChooser(viewModel.onPickPhoto(), "Select Picture"));
            }
        });
    }

    @NonNull
    private ActivityResultLauncher<Intent> getGalleryLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri photoUri = result.getData().getData();
                    viewModel.setSelectedImage(viewModel.loadFromUri(photoUri, ReviewsActivity.this));
                    binding.ivPhoto.setVisibility(View.VISIBLE);
                    binding.ivPhoto.setImageBitmap(viewModel.getSelectedImage());
                } else {
                    Toast.makeText(ReviewsActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}