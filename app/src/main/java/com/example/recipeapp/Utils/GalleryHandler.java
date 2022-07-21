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

import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GalleryHandler {
    private static final String NAME = "myImage";
    private static final String INTENT_TYPE = "image/*";

    // Trigger gallery selection for a photo
    public static Intent onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent();
        intent.setType(INTENT_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public static ActivityResultLauncher<Intent> getGalleryLauncher(ActivityResultCallback<ActivityResult> callback, ComponentActivity activity) {
        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);
    }

    public static Bitmap loadFromUri(Uri photoUri, Activity loadActivity) {
        Bitmap image = null;
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(loadActivity.getContentResolver(), photoUri);
            image = ImageDecoder.decodeBitmap(source);
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
