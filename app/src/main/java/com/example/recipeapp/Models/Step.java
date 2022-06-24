package com.example.recipeapp.Models;

import com.google.gson.annotations.SerializedName;

public class Step {
    @SerializedName("number")
    public int number;
    @SerializedName("step")
    public String step;
}
