package com.example.recipeapp.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.recipeapp.Models.Recipe;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    public static final String NAME = "RecipeDataBase";
}
