package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lon")
    public double lon;

    @SerializedName("lat")
    public double lat;
}