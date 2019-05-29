package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public double speed;

    @SerializedName("deg")
    public int deg;
}
