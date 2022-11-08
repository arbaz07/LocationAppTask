package com.example.locationappassignment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView companyLogo;
    private TextView companyName;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            getSupportActionBar().hide(); //Hide the action bar from the Splash Screen!

            companyLogo = findViewById(R.id.companyLogo);
            companyName = findViewById(R.id.companyName);

            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            animation();

            splashScreenTimer();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * This function is used for animating company logo and the text
     */
    private void animation() {
        try {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setDuration(SPLASH_DISPLAY_LENGTH);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (float) animation.getAnimatedValue();
                    companyLogo.setAlpha(alpha);
                    companyName.setAlpha(alpha);
                }
            });
            valueAnimator.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * This function will stop the Splash Screen for 3 seconds
     * Check the location and GPS permissions and show the screen accordingly
     */
    private void splashScreenTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startActivity(new Intent(Splash.this, Home.class));
                } else {
                    startActivity(new Intent(Splash.this, LocationPermission.class));
                }
                finish();
            }
        }, 3 * 1000); // wait for 3 seconds
    }
}