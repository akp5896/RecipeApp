package com.example.recipeapp.Retrofit;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

import retrofit2.http.PATCH;

public class InstructionEnvelope<T> {
    @SerializedName("steps")
    public T results;
}
