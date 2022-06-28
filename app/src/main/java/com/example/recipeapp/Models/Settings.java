package com.example.recipeapp.Models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.recipeapp.EditPreferencesActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Settings {
    private static String diet;
    private static List<String> cuisines;
    private static HashSet<String> intolerances;
    private static HashSet<String> banned;

    public static void getSavedSettings(Context context) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);
        diet = pref.getString(EditPreferencesActivity.DIET, null);
        cuisines = new ArrayList<>(pref.getStringSet(EditPreferencesActivity.CUISINES, null));
        intolerances = (HashSet<String>) pref.getStringSet(EditPreferencesActivity.INTOLERANCES, null);
        banned = (HashSet<String>) pref.getStringSet(EditPreferencesActivity.BANNED, null);
    }

    public static String getDiet() {
        return (diet.equals("")) ? null : diet;
    }

    public static void setDiet(String diet) {
        Settings.diet = diet;
    }

    public static List<String> getCuisines() {
        if(cuisines.size() == 0)
            return null;
        return cuisines;
    }

    public static void setCuisines(HashSet<String> cuisines) {
        Settings.cuisines = new ArrayList<>(cuisines);
    }

    public static String getIntolerances() {
        if(intolerances.size() == 0) {
            return null;
        }
        return String.join(",", intolerances);
    }

    public static void setIntolerances(HashSet<String> intolerances) {
        Settings.intolerances = intolerances;
    }

    public static String getBannedString() {
        if(banned.size() == 0)
            return null;
        return String.join(",", banned);
    }

    public static HashSet<String> getBanned() {
        return banned;
    }

    public static void setBanned(HashSet<String> banned) {
        Settings.banned = banned;
    }

    public static boolean checkIntolerance(String intolerance) {
        return intolerances.contains(intolerance);
    }

    public static boolean containsIntolerance(String intolerance) {
        return intolerances.contains(intolerance);
    }
}
