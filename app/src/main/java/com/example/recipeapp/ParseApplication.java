package com.example.recipeapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.example.recipeapp.Models.Parse.CuisineCounter;
import com.example.recipeapp.Models.Parse.DietCounter;
import com.example.recipeapp.Models.Parse.Preferences;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Utils.NotificationAlarmReceiver;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
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

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = NotificationAlarmReceiver.CHANNEL_NAME;
        String description = NotificationAlarmReceiver.CHANNEL_DESCRIPTION;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NotificationAlarmReceiver.CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}
