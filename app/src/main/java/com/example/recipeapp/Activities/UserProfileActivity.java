package com.example.recipeapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.recipeapp.Adapters.RecipesAdapter;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.databinding.ActivityAddRecipeBinding;
import com.example.recipeapp.databinding.ActivityUserProfileBinding;
import com.example.recipeapp.viewmodels.AddRecipeViewModel;
import com.example.recipeapp.viewmodels.UserProfileViewModel;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    UserProfileViewModel viewModel;
    public static final String USERNAME = "username";
    RecipesAdapter adapter = new RecipesAdapter(new ArrayList<>(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        binding.setViewModel(viewModel);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecipes.setAdapter(adapter);

        String username = getIntent().getStringExtra(USERNAME);
        viewModel.loadUser(username);
        binding.tvName.setText(username);

        viewModel.bio.observe(this, bio -> binding.tvBio.setText(bio));

        viewModel.image.observe(this, parseFile -> {
            if(parseFile != null) {
                Glide.with(UserProfileActivity.this)
                        .load(parseFile.getUrl())
                        .transform(new RoundedCorners(50))
                        .into(binding.ivProfilePic);
            }
        });

        viewModel.recipesByUser.observe(this, recipes -> adapter.updateList(recipes));
    }
}