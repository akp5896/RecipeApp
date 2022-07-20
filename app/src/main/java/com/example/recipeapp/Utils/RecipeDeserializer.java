package com.example.recipeapp.Utils;

import com.example.recipeapp.Models.API.Step;
import com.example.recipeapp.Models.Parse.Taste;
import com.example.recipeapp.Models.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeDeserializer implements JsonDeserializer<Recipe> {
    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Recipe recipe = new Gson().fromJson(jsonObject, Recipe.class);
        JsonArray analyzedInstructions = jsonObject.get("analyzedInstructions").getAsJsonArray();
        if(analyzedInstructions.size() == 0) {
            return recipe;
        }
        JsonArray jsonArray = analyzedInstructions.get(0).getAsJsonObject().get("steps").getAsJsonArray();
        List<Step> instructionsList = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++) {
            instructionsList.add(new Gson().fromJson(jsonArray.get(i), Step.class));
        }
        recipe.setAnalyzedInstructions(instructionsList);
        return recipe;
    }
}
