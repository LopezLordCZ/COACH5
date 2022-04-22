package com.example.coach5;

import android.app.Service;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


    public class LocationService extends Service {
        FusedLocationProviderClient fusedLocationProviderClient;
        //Location for distance
        LocationCallback locationCallback;
        //Latitude
        public double latitudeUser;
        //Longitude
        public double longitudeUser;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            //Get location
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    //Print latitude and longitude
                    Log.d("mylog", "Lat is: " + locationResult.getLastLocation().getLatitude()
                            + ", " + "Lng is: " + locationResult.getLastLocation().getLongitude());

                    latitudeUser = locationResult.getLastLocation().getLatitude();
                    longitudeUser = locationResult.getLastLocation().getLongitude();
                }
            };
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //Get location
            requestLocation();
            //On start
            return super.onStartCommand(intent, flags, startId);
        }

        private void requestLocation() {
            //To get new location
            LocationRequest locationRequest = new LocationRequest();
            //locationRequest.setInterval(20000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }


