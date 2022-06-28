package com.example.recipeapp.Retrofit;

import com.google.gson.annotations.SerializedName;

public class SubEnvelope<T> {
    @SerializedName("substitutes")
    public T results;
    @SerializedName("status")
    public String status;
}
