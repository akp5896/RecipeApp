package com.example.recipeapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.Models.Parse.LiveUserData;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.Repositories.RecipesRepository;
import com.example.recipeapp.Repositories.UserRepository;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class UserProfileViewModel extends ViewModel {

    public MutableLiveData<String> bio;
    public MutableLiveData<ParseFile> image;
    public MutableLiveData<List<Recipe>> recipesByUser;

    public void loadUser(String username) {
        LiveUserData userByUsername = UserRepository.getRepository().getUserByUsername(username);
        bio = userByUsername.getBio();
        image = userByUsername.getImage();

        recipesByUser = RecipesRepository.getRepository().fetchParse(username);
    }
}
