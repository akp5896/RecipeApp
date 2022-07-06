package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.recipeapp.Adapters.IngredientFilterAdapter;
import com.example.recipeapp.Models.Ingredient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.recipeapp.Adapters.StepsAdapter;
import com.example.recipeapp.Models.API.RecipeWidget;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Retrofit.RecipeApi;
import com.example.recipeapp.Retrofit.RetrofitClientInstance;
import com.example.recipeapp.Utils.ShareRecipe;
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.parse.ParseUser;
import com.example.recipeapp.databinding.ActivityDetailsBinding;
import com.example.recipeapp.viewmodels.DetailsViewModel;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DETAILS ACTIVITY";
    ActivityDetailsBinding binding;
    public static final String RECIPE = "recipe";
    public static final String INGREDIENTS = "ingredients";

    Recipe recipe;
    DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        recipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE));
        viewModel = new DetailsViewModel(recipe);
        binding.setViewModel(viewModel);

        viewModel.showIngredients.observe(this, ingredients -> {
            Intent i = new Intent(DetailsActivity.this, IngredientsActivity.class);
            i.putExtra(DetailsActivity.INGREDIENTS, Parcels.wrap(ingredients));
            startActivity(i);
        });

        binding.options.like.setOnClickListener(v -> viewModel.onLike());
        binding.options.share.setOnClickListener(v -> onShare());

        viewModel.liked.observe(this, aBoolean -> Toast.makeText(DetailsActivity.this, R.string.liked, Toast.LENGTH_SHORT).show());

        viewModel.widgetLoaded.observe(this, url -> Glide.with(getApplicationContext()).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri localBitmapUri = getLocalBitmapUri(resource);
                shareIntent.putExtra(Intent.EXTRA_STREAM, localBitmapUri);
                shareIntent.setType("image/*");
                Intent chooser = Intent.createChooser(shareIntent, getString(R.string.share_recipe));
                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, localBitmapUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivity(chooser);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        }));
    }

    private void onShare() {
        new AlertDialog.Builder(DetailsActivity.this)
                .setTitle(R.string.choose_sharing)
                .setMessage(R.string.share_question)
                .setPositiveButton(
                        R.string.share_widget, (dialog, which) -> viewModel.shareWidget())
                .setNegativeButton(R.string.share_recipe, (dialog, which) -> shareRecipe())
                .setIcon(android.R.drawable.ic_menu_share)
                .show();
    }

    private void shareRecipe() {
        Toast.makeText(this, R.string.sharing_started, Toast.LENGTH_LONG).show();
        new ShareRecipe().startAdvertising(this, ParseUser.getCurrentUser().getUsername(), recipe);
    }


    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(
                    DetailsActivity.this,
                    "com.example.recipeapp.provider",
                    file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}