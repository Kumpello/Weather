package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherMain {
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("name")
    public String name;

    @SerializedName("weather")
    public List<Weather> weather;
}
