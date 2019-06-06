package com.example.weather.network;

import com.example.weather.models.WeatherMain;
import com.example.weather.models.WeatherShortForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather")
    Call<WeatherMain> getWeather(@Query("q") String city,@Query("units") String units, @Query("appid") String appid);
    @GET("data/2.5/forecast")
    Call<WeatherShortForecast> getWeatherForecast(@Query("q") String city,@Query("units") String units, @Query("appid") String appid);
    @GET("data/2.5/weather")
    Call<WeatherMain> getWeatherbyGPS(@Query("lat") String lat,@Query("lon") String lon,@Query("units") String units, @Query("appid") String appid);
    @GET("data/2.5/forecast")
    Call<WeatherShortForecast> getWeatherFoecastbyGPS(@Query("lat") String lat,@Query("lon") String lon,@Query("units") String units, @Query("appid") String appid);

}
