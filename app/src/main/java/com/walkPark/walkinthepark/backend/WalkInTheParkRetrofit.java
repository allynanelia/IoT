package com.walkPark.walkinthepark.backend;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalkInTheParkRetrofit {

    private static final String TAG = "WalkInTheParkRetrofit";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);

            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://witp-api.herokuapp.com/api/")
                    .build();

            Log.d(TAG, "Retrofit instance created");
        }
        return retrofit;
    }

    private WalkInTheParkRetrofit() {}
}
