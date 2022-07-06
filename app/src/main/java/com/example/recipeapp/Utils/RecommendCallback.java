package com.example.recipeapp.Utils;

import com.example.recipeapp.Models.Recipe;

import java.util.List;

public interface RecommendCallback {
    void OnRecommendationReturned(List<Recipe> recipes);
}
