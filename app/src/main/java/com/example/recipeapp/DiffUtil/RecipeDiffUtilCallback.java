package com.example.recipeapp.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.recipeapp.Models.Recipe;

import java.util.List;
import java.util.Objects;

public class RecipeDiffUtilCallback extends DiffUtil.Callback {
    private final List<Recipe> oldRecipes;
    private final List<Recipe> newRecipes;

    public RecipeDiffUtilCallback(List<Recipe> oldRecipes, List<Recipe> newRecipes) {
        this.oldRecipes = oldRecipes;
        this.newRecipes = newRecipes;
    }

    @Override
    public int getOldListSize() {
        return oldRecipes.size();
    }

    @Override
    public int getNewListSize() {
        return newRecipes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldRecipes.get(oldItemPosition).getId(), newRecipes.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Since recipes items are immutable
        return true;
    }
}
