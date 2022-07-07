package com.example.recipeapp.viewmodels;

import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Adapters.ReviewsAdapter;
import com.parse.ParseFile;

import java.util.List;


public class ReviewItemViewModel {
    public String authorName;
    public String body;
    public ParseFile media;

    public ReviewItemViewModel(String authorName, String body, ParseFile media) {
        this.authorName = authorName;
        this.body = body;
        this.media = media;
    }

    @androidx.databinding.BindingAdapter("parseImage")
    public static void bindItemViewModels(ImageView imageView, ParseFile media) {
        if(media == null) {
            imageView.setVisibility(View.GONE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        Glide.with(imageView.getContext()).load(media.getUrl()).into(imageView);
    }
}