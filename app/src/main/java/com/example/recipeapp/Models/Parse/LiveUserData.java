package com.example.recipeapp.Models.Parse;

import androidx.lifecycle.MutableLiveData;

import com.parse.ParseFile;

public class LiveUserData {
    MutableLiveData<String> bio;
    MutableLiveData<ParseFile> image;

    public LiveUserData(MutableLiveData<String> bio, MutableLiveData<ParseFile> image) {
        this.bio = bio;
        this.image = image;
    }

    public MutableLiveData<String> getBio() {
        return bio;
    }

    public MutableLiveData<ParseFile> getImage() {
        return image;
    }
}
