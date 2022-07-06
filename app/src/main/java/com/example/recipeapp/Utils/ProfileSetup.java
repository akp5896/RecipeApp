package com.example.recipeapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.recipeapp.Fragments.BiographyFragment;
import com.example.recipeapp.R;
import com.example.recipeapp.databinding.ProfileLayoutBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileSetup {

    public static final String USER_BIO = "bio";
    private static final String USER_PROFILE_PICTURE = "profilePicture";


    public static void initialize(ProfileLayoutBinding binding, AppCompatActivity activity) {
        Context context = activity.getApplicationContext();
        binding.ivPreferences.setOnClickListener(v -> activity.startActivity(new Intent(context, EditPreferencesActivity.class)));
        setBanner(context, binding);
        try {
            binding.tvName.setText(ParseUser.getCurrentUser().fetch().getUsername());
            String bio = ParseUser.getCurrentUser().fetch().getString(USER_BIO);
            binding.tvBio.setText((bio == null || bio.equals("")) ? context.getString(R.string.no_bio)  : bio);
            binding.tvBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = activity.getSupportFragmentManager();
                    BiographyFragment biographyFragment = BiographyFragment.newInstance(bio, binding);
                    biographyFragment.show(fm, BiographyFragment.BIO_TAG);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public static void setBanner(Context context, ProfileLayoutBinding binding) {
        try {
            ParseFile parseFile = ParseUser.getCurrentUser().fetch().getParseFile(USER_PROFILE_PICTURE);
            if(parseFile != null) {
                Glide.with(context)
                        .load(parseFile.getUrl())
                        .transform(new RoundedCorners(50))
                        .into(binding.ivProfilePic);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    public static ActivityResultCallback<ActivityResult> getHeaderCallback(ProfileLayoutBinding binding, Activity loadActivity) {
        return result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if ((result.getData() != null)) {
                    Uri photoUri = result.getData().getData();
                    // Load the image located at photoUri into selectedImage
                    Bitmap selectedImage = GalleryHandler.loadFromUri(photoUri, loadActivity);

                    ParseUser user = ParseUser.getCurrentUser();
                    user.put(USER_PROFILE_PICTURE, GalleryHandler.bitmapToParseFile(selectedImage));
                    user.saveInBackground(e -> {
                        // Load the selected image into a preview
                        binding.ivProfilePic.setImageBitmap(selectedImage);
                    });
                }
            }
        };
    }
}
