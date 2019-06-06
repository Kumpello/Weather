package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

public class List {
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("weather")
    public java.util.List<Weather> weather;
}
