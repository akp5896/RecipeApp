package com.example.recipeapp.Models.API;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@AutoValue
public abstract class Step implements Parcelable{
    @SerializedName("number")
    public abstract int getNumber();
    @SerializedName("step")
    public abstract String getStep();

    public static TypeAdapter<Step> typeAdapter(Gson gson) {
        return new AutoValue_Step.GsonTypeAdapter(gson);
    }

}
