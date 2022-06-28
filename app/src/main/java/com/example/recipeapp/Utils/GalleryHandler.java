package com.example.recipeapp.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GalleryHandler {
    private static final String NAME = "myImage";
    public ActivityResultLauncher<Intent> launcher;
    ComponentActivity activity;

    public GalleryHandler(AppCompatActivity activity, ActivityResultCallback<ActivityResult> callback) {
        this.activity = activity;
        getGalleryLauncher(callback);
    }

    // Trigger gallery selection for a photo
    public Intent onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public void getGalleryLauncher(ActivityResultCallback<ActivityResult> callback) {
        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);
    }

    public static Bitmap loadFromUri(Uri photoUri, Activity loadActivity) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(loadActivity.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(loadActivity.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Nullable
    public static ParseFile bitmapToParseFile(Bitmap image) {
        if(image == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        ParseFile result = new ParseFile(NAME, bitmapBytes);
        return result;
    }
}
