package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherShortForecast {
    @SerializedName("list")
    public List<com.example.weather.models.List> list;

}
