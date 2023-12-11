package com.example.smarthomeku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

    public final class SplashActivity extends AppCompatActivity {
        private final long SPLASH_DELAY = 2000L;

        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.splash_screen);
            (new Handler()).postDelayed((Runnable)(new Runnable() {
                public final void run() {
                    Intent intent = new Intent((Context) com.example.smarthomeku.SplashActivity.this, LoginActivity.class);
                    com.example.smarthomeku.SplashActivity.this.startActivity(intent);
                    com.example.smarthomeku.SplashActivity.this.finish();
                }
            }), this.SPLASH_DELAY);
        }
    }
