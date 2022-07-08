package com.example.recipeapp.Room;

import android.net.InetAddresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.recipeapp.Models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    LiveData<List<Recipe>> getRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe... recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT COUNT(*) FROM Recipe where id = :id")
    Integer getRecipeById(Long id);
}
