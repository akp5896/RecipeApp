package com.example.recipeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import com.example.recipeapp.Adapters.ReviewsAdapter;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityReviewsBinding;
import com.example.recipeapp.viewmodels.ReviewItemViewModel;
import com.example.recipeapp.viewmodels.ReviewsViewModel;
import com.example.recipeapp.viewmodels.SignUpViewModel;

import java.io.File;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = "ReviewsActivity";
    ActivityReviewsBinding binding;
    ReviewsViewModel viewModel;
    ActivityResultLauncher<Intent> galleryLauncher;
    ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewsBinding.inflate(getLayoutInflater());
        String title = getIntent().getStringExtra(DetailsActivity.RECIPE);
        Long id = getIntent().getLongExtra(DetailsActivity.RECIPE_ID, 0);
        viewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.setTitle(title);
        viewModel.setBody(getString(R.string.write_review));
        viewModel.setReviewTo(id);
        
        setContentView(binding.getRoot());
        galleryLauncher = getGalleryLauncher();

        viewModel.takePicture.observe(this, takePicture -> galleryLauncher.launch(Intent.createChooser(viewModel.onPickPhoto(), "Select Picture")));
        viewModel.reviewSaved.observe(this, s -> Toast.makeText(ReviewsActivity.this, s, Toast.LENGTH_LONG).show());

        adapter = new ReviewsAdapter();
        binding.rvReviews.setAdapter(adapter);

        viewModel.getData().observe(this, reviewItemViewModels -> adapter.updateItems(reviewItemViewModels));

        viewModel.post.observe(this, reviewItemViewModel -> {
            adapter.addAtTheBeginning(reviewItemViewModel);
            binding.rvReviews.smoothScrollToPosition(0);
            binding.ivPhoto.setVisibility(View.GONE);
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