package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.models.WeatherMain;
import com.example.weather.network.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements View.OnClickListener {

    private static String API_KEY = "4a398b11f0abe2249e923869da43fa91";
    private String city;

    TextView cityTextView;
    TextView lowestTemp;
    TextView temp;
    TextView highestTemp;
    ImageView background, arrow;
    TextView pressure, humidity, clouds, wind;
    TextView textView;
    LocationManager locationManager;

    Button findButton;
    ImageView gpsButton;

    View view;

    double longitude, latitude;

    public void setBackground(String weatherCode) {
        switch (weatherCode) {
            case "01d":
            case "01n":
                background.setImageResource(R.drawable.clearsky);
                break;
            case "02d":
            case "02n":
                background.setImageResource(R.drawable.fewclouds);
                break;
            case "03d":
            case "03n":
                background.setImageResource(R.drawable.scatteredclouds);
                break;
            case "04d":
            case "04n":
                background.setImageResource(R.drawable.brokenclouds);
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
                background.setImageResource(R.drawable.rain);
                break;
            case "11d":
            case "11n":
                background.setImageResource(R.drawable.thunderstorm);
                break;
            case "13d":
            case "13n":
                background.setImageResource(R.drawable.snow);
                break;
            case "50d":
            case "50n":
                background.setImageResource(R.drawable.mist);
                break;
        }
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getActivity().getApplicationContext(), "GPS Error", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getActivity().getApplicationContext(), "GPS", Toast.LENGTH_LONG).show();
        }
    }

    public void setWeather(Response<WeatherMain> response){
        setBackground(response.body().weather.get(0).icon);
        lowestTemp.setText(getString(R.string.low_temp)+":\n" + String.valueOf(response.body().main.temp_min) + " \u2103");
        temp.setText(getString(R.string.temp)+":\n" + String.valueOf(response.body().main.temp) + " \u2103");
        highestTemp.setText(getString(R.string.high_temp)+":\n" + String.valueOf(response.body().main.temp_max) + " \u2103");
        pressure.setText(getString(R.string.pressure)+":\n" + response.body().main.pressure + " hPa");
        humidity.setText(getString(R.string.humidity)+":\n" + response.body().main.humidity + "%");
        textView.setText(capitalizeFirstLetter(response.body().weather.get(0).description));
        clouds.setText(getString(R.string.clouds)+":\n" + response.body().clouds.all + "%");
        wind.setText(getString(R.string.wind)+":\n" + String.valueOf(response.body().wind.speed) + " m/s");

        arrow.setPivotX(arrow.getWidth() / 2);
        arrow.setPivotY(arrow.getHeight() / 2);
        arrow.setRotation(360 - response.body().wind.deg);
    }

    public void findWeather(View view) {
        city = cityTextView.getText().toString();

        WeatherAPI.getInstance().getWeather(city, "metric", API_KEY).enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                setWeather(response);

                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


    public void findLocation(View view) {


        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Toast.makeText(getActivity().getApplicationContext(), "Error with permissions", Toast.LENGTH_LONG).show();
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


        WeatherAPI.getInstance().getWeatherbyGPS(String.valueOf(latitude), String.valueOf(longitude),"metric", API_KEY).enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                setWeather(response);

                cityTextView.setText(response.body().name+","+response.body().sys.country);

                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Error getting position", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main, container, false);

        cityTextView = view.findViewById(R.id.editText);
        lowestTemp = view.findViewById(R.id.lowestTemp);
        temp = view.findViewById(R.id.temp);
        highestTemp = view.findViewById(R.id.highestTemp);
        background = view.findViewById(R.id.background);
        pressure = view.findViewById(R.id.pressure);
        humidity = view.findViewById(R.id.humidity);
        textView = view.findViewById(R.id.textView);
        clouds = view.findViewById(R.id.clouds);
        wind = view.findViewById(R.id.wind);
        arrow = view.findViewById(R.id.arrow);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, 420);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        findButton = view.findViewById(R.id.button);
        gpsButton = view.findViewById(R.id.gps);
        findButton.setOnClickListener(this);
        gpsButton.setOnClickListener(this);


        return view;
    }

   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                findWeather(v);
                break;
            case R.id.gps:
                findLocation(v);
                break;
            default:
                break;
        }

    }



}

