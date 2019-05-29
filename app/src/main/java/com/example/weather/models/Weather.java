package com.example.weather.models;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    public int id;

    @SerializedName("description")
    public String description;

    @SerializedName("icon")
    public String icon;

}