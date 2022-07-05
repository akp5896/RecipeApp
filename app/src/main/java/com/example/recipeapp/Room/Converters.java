package com.example.recipeapp.Room;

import androidx.room.TypeConverter;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Retrofit.InstructionEnvelope;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<String> stringListFromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String stringFromArrayList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Ingredient> ingredientListFromString(String value) {
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String ingredientFromArrayList(List<Ingredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<InstructionEnvelope<List<Step>>> analyzedInstructionsFromString(String value) {
        Type listType = new TypeToken<List<InstructionEnvelope<List<Step>>>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String stringFromAnalyzedInstructions(List<InstructionEnvelope<List<Step>>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
