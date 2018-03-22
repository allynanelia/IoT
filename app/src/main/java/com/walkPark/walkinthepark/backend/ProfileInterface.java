package com.walkPark.walkinthepark.backend;

import com.walkPark.walkinthepark.models.ProfileResponse;
import com.walkPark.walkinthepark.models.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by singy on 3/22/2018.
 */

public interface ProfileInterface {
    @GET("player/{id}")
    Call<ProfileResponse> getProfile(@Path("id") int playerID);
}
