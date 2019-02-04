package com.exception.mylatlong;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    android.location.LocationListener locationListener;
    static Location userCurrLocation;
    FusedLocationProviderClient mFusedLocationClient = null;
    protected LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocation();
    }

    public void getLocation(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView lat = findViewById(R.id.lat);
                TextView lon = findViewById(R.id.lon);
                lat.setText(location.getLatitude() + "");
                lon.setText(location.getLongitude() + "");
                System.out.println("lat: " + location.getLatitude());
                System.out.println("lon: " + location.getLongitude());
                System.out.println("provider: " + location.getProvider());
                System.out.println("accuracy: " + location.getAccuracy());
                System.out.println("time: " + location.getTime());
                System.out.println("location: " + location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // if we don't have the permission we will request the permission to the user
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                // if we already have the permission, we will get te location.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                /***** get the old know location if there have any ******/
                userCurrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }else {

            // if we already have the permission, we will get te location.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            /***** get the old know location if there have any ******/
            userCurrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            TextView lat = findViewById(R.id.lat);
            TextView lon = findViewById(R.id.lon);
            lat.setText(userCurrLocation.getLatitude() + "");
            lon.setText(userCurrLocation.getLongitude() + "");

        }
    }

}
