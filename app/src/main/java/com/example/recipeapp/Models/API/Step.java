package com.example.recipeapp.Models.API;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Step{
    @SerializedName("number")
    int number;
    @SerializedName("step")
    String step;

    public Step() {}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Step(int number, String step) {
        this.number = number;
        this.step = step;
    }
}
