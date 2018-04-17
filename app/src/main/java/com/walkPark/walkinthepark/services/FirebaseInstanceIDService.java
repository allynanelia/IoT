package com.walkPark.walkinthepark.services;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.DeviceToken;
import com.walkPark.walkinthepark.models.RouteCheckPointResponse;
import com.walkPark.walkinthepark.models.TokenResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nanelia on 12/4/18.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Prefs.setToken(token);
        final RouteInterface routeInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(RouteInterface.class);

        Call<TokenResponse> call = routeInterface.registerDevice(new DeviceToken(token));
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG,"Token sucessfully registered");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d(TAG,"FAILED TO REGISTER TOKEN");
                Toast.makeText(getApplicationContext(),"Error Loading API",
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
}
