package com.example.locationappassignment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationPermission extends AppCompatActivity implements LocationListener {
    private Button allowPermission;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);
        try {
            getSupportActionBar().hide(); //Hide the action bar from the Splash Screen!

            allowPermission = findViewById(R.id.allowPermission);
            allowPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkLocationPermission();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * This function will check Location permission
     * Function calling for GPS Enable check
     */
    private void checkLocationPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(LocationPermission.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LocationPermission.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                checkGPSLocation();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * On Activity Result of Location Permission
     * Show toast message on permission status
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(LocationPermission.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        checkGPSLocation();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * This function will check either the device GPS is on or not.
     * Function calling of get current location
     */
    private void checkGPSLocation() {
        try {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                getLocation();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Display Dialog-box for enabling GPS Location
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable to use the app!")
                .setCancelable(false)
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Request Current Location Of User
     */
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will update the location and open home activity
     *
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            SetAndGetData.getInstance().setLat(location.getLatitude());
            SetAndGetData.getInstance().setLng(location.getLongitude());

            startActivity(new Intent(this, Home.class));
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will show toast message on GPS Disabled
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}