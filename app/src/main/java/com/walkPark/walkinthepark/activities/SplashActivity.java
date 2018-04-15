package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.walkPark.walkinthepark.R;
import com.walkPark.walkinthepark.WalkInTheParkGson;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.RouteResponse;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boon Sing on 01-Mar-18.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, TransitActivity.class));
                finish();
            }
        },1000);
    }
}
