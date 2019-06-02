package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.models.WeatherMain;
import com.example.weather.network.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    double longitude, latitude;

    private DrawerLayout drawer;

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
            Toast.makeText(getApplicationContext(), "GPS Error", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), "GPS", Toast.LENGTH_LONG).show();
        }
    }

    public void findWeather(View view) {
        city = cityTextView.getText().toString();

        WeatherAPI.getInstance().getWeather(city, "metric", API_KEY).enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                setBackground(response.body().weather.get(0).icon);
                lowestTemp.setText("Lowest Temperature:\n" + String.valueOf(response.body().main.temp_min) + " \u2103");
                temp.setText("Temperature:\n" + String.valueOf(response.body().main.temp) + " \u2103");
                highestTemp.setText("Highest Temperature:\n" + String.valueOf(response.body().main.temp_max) + " \u2103");
                pressure.setText("Pressure:\n" + response.body().main.pressure + " hPa");
                humidity.setText("Humidity:\n" + response.body().main.humidity + "%");
                textView.setText(capitalizeFirstLetter(response.body().weather.get(0).description));
                clouds.setText("Clouds:\n" + response.body().clouds.all + "%");
                wind.setText("Wind:\n" + String.valueOf(response.body().wind.speed) + " m/s");

                arrow.setPivotX(arrow.getWidth() / 2);
                arrow.setPivotY(arrow.getHeight() / 2);
                arrow.setRotation(360 - response.body().wind.deg);

                Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void findLocation(View view) {

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Toast.makeText(getApplicationContext(), "Error with permissions", Toast.LENGTH_LONG).show();
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


        WeatherAPI.getInstance().getWeatherbyGPS(String.valueOf(latitude), String.valueOf(longitude),"metric", API_KEY).enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                setBackground(response.body().weather.get(0).icon);
                lowestTemp.setText("Lowest Temperature:\n" + String.valueOf(response.body().main.temp_min) + " \u2103");
                temp.setText("Temperature:\n" + String.valueOf(response.body().main.temp) + " \u2103");
                highestTemp.setText("Highest Temperature:\n" + String.valueOf(response.body().main.temp_max) + " \u2103");
                pressure.setText("Pressure:\n" + response.body().main.pressure + " hPa");
                humidity.setText("Humidity:\n" + response.body().main.humidity + "%");
                textView.setText(capitalizeFirstLetter(response.body().weather.get(0).description));
                clouds.setText("Clouds:\n" + response.body().clouds.all + "%");
                wind.setText("Wind:\n" + String.valueOf(response.body().wind.speed) + " m/s");

                cityTextView.setText(response.body().name+","+response.body().sys.country);

                arrow.setPivotX(arrow.getWidth() / 2);
                arrow.setPivotY(arrow.getHeight() / 2);
                arrow.setRotation(360 - response.body().wind.deg);

                Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = findViewById(R.id.editText);
        lowestTemp = findViewById(R.id.lowestTemp);
        temp = findViewById(R.id.temp);
        highestTemp = findViewById(R.id.highestTemp);
        background = findViewById(R.id.background);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        textView = findViewById(R.id.textView);
        clouds = findViewById(R.id.clouds);
        wind = findViewById(R.id.wind);
        arrow = findViewById(R.id.arrow);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 420);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_first);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_first:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_second:
                        Toast.makeText(this, "Ebin xD", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_third:
                        Toast.makeText(this, "Ebin xD", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_fourth:
                Toast.makeText(this, "Ebin xD", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}