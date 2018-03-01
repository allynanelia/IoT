package com.walkPark.walkinthepark.backend;

import com.walkPark.walkinthepark.models.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Boon Sing on 01-Mar-18.
 */

public interface RouteInterface {
    @GET("routes")
    Call<RouteResponse> getAllRoutes();
}
