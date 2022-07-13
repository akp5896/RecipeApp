package com.example.recipeapp.Room;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.recipeapp.Models.Recipe;

@Database(entities = {Recipe.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase recipeDatabase;

    public abstract RecipeDao recipeDao();

    public static final String NAME = "RecipeDataBase";

    public static RecipeDatabase getRecipeDatabase() {
        return recipeDatabase;
    }

    public static void createRecipeDatabase(Application application)
    {
        if(recipeDatabase == null) {
            recipeDatabase = Room.databaseBuilder(application, RecipeDatabase.class,
                    RecipeDatabase.NAME).fallbackToDestructiveMigration().build();
        }
    }
}
