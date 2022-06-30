package com.example.recipeapp.Utils;

import com.example.recipeapp.Models.Parse.Taste;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TasteDeserializer implements JsonDeserializer<Taste> {


    @Override
    public Taste deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        HashMap<String, Double> componentValues = new HashMap<>();
        for(String component : Taste.COMPONENTS) {
            componentValues.put(component, jsonObject.get(component).getAsDouble());
        }
        Taste taste = new Taste();
        taste.setComponentValues(componentValues);
        return taste;
    }
}
