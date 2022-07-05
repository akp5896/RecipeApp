package com.example.recipeapp;

import android.app.Application;

import androidx.room.Room;

import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Parse.CuisineCounter;
import com.example.recipeapp.Models.Parse.DietCounter;
import com.example.recipeapp.Room.RecipeDao;
import com.example.recipeapp.Room.RecipeDatabase;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    RecipeDatabase recipeDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Preferences.class);
        ParseObject.registerSubclass(Taste.class);
        ParseObject.registerSubclass(DietCounter.class);
        ParseObject.registerSubclass(CuisineCounter.class);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID) // should correspond to Application Id env variable
                .clientKey(BuildConfig.PARSE_CLIENT_KEY)  // should correspond to Client key env variable
                .server("https://parseapi.back4app.com").build());

        recipeDatabase = Room.databaseBuilder(this, RecipeDatabase.class,
                RecipeDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public RecipeDatabase getRecipeDatabase() {
        return recipeDatabase;
    }
}
