package com.example.recipeapp.Retrofit;

import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Utils.TasteDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.spoonacular.com";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(Taste.class, new TasteDeserializer())
                            .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
