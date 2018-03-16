package com.walkPark.walkinthepark.backend;

import com.walkPark.walkinthepark.models.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Boon Sing on 01-Mar-18.
 */

public interface RouteInterface {
    @GET("routes/player/{id}")
    Call<RouteResponse> getAllRoutes(@Path("id") int playerID);


}
