package com.walkPark.walkinthepark.backend;

import com.walkPark.walkinthepark.models.LeaderboardResponse;
import com.walkPark.walkinthepark.models.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LeaderBoardInterface {

    @GET("leaderboard")
    Call<LeaderboardResponse> getLeaderboard();
}
