package com.example.recipeapp.Models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.recipeapp.EditPreferencesActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Stores local user preferences(diets, cuisines, intolerances and banned ingredients in the shared preferences)
 * They are used in recommendation and search
 */
public class Settings {
    private static String diet;
    private static List<String> cuisines;
    private static HashSet<String> intolerances;
    private static HashSet<String> banned;

    /**
     * Retrieves saved user setting from shared preferences
     * @param context
     */
    public static void getSavedSettings(Context context) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);
        diet = pref.getString(EditPreferencesActivity.DIET, "");
        cuisines = new ArrayList<>(pref.getStringSet(EditPreferencesActivity.CUISINES, new HashSet<>()));
        intolerances = (HashSet<String>) pref.getStringSet(EditPreferencesActivity.INTOLERANCES, new HashSet<>());
        banned = (HashSet<String>) pref.getStringSet(EditPreferencesActivity.BANNED, new HashSet<>());
    }

    public static String getDiet() {
        return (diet.equals("")) ? null : diet;
    }

    public static void setDiet(String diet) {
        Settings.diet = diet;
    }

    public static List<String> getCuisines() {
        return cuisines;
    }

    public static void setCuisines(HashSet<String> cuisines) {
        Settings.cuisines = new ArrayList<>(cuisines);
    }

    public static String getIntolerances() {
        return String.join(",", intolerances);
    }

    public static void setIntolerances(HashSet<String> intolerances) {
        Settings.intolerances = intolerances;
    }

    public static String getBannedString() {
        return String.join(",", banned);
    }

    public static HashSet<String> getBanned() {
        return banned;
    }

    public static void setBanned(HashSet<String> banned) {
        Settings.banned = banned;
    }

    public static boolean containsIntolerance(String intolerance) {
        return intolerances.contains(intolerance);
    }
}
