package com.walkPark.walkinthepark.backend;

import com.walkPark.walkinthepark.models.CheckpointDetails;
import com.walkPark.walkinthepark.models.DeviceToken;
import com.walkPark.walkinthepark.models.RouteCheckPointResponse;
import com.walkPark.walkinthepark.models.RouteResponse;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Boon Sing on 01-Mar-18.
 */

public interface RouteInterface {
    @GET("routes")
    Call<RouteResponse> getRoutes();

    @GET("routes/player/{id}")
    Call<RouteResponse> getAllRoutes(@Path("id") int playerID);

    @POST("routes/player/{id}")
    Call<RouteResponse> getAllRoutesWithToken(@Path("id") int playerID,
                                              @Body DeviceToken token);

    @POST("player/checkpoint")
    Call<RouteCheckPointResponse> checkpointComplete(@Body CheckpointDetails checkpointDetails);

    @POST("device/registration")
    Call<TokenResponse> registerDevice(@Body DeviceToken token);
}
