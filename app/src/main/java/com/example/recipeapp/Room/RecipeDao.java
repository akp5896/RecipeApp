package com.example.recipeapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.recipeapp.Models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    List<Recipe> getRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe... recipe);
}
