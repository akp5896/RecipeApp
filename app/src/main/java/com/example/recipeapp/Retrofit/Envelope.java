package com.example.flex3;

import com.google.gson.annotations.SerializedName;

public class Envelope<T> {
    @SerializedName("results")
    T results;
}
