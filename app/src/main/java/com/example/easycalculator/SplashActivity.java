package com.example.easycalculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to delay for 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 2 seconds, start the MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish SplashActivity so it's not in the back stack
            }
        }, 5000); // 2000 milliseconds = 2 seconds
    }
}