package com.walkPark.walkinthepark.models;

import java.util.List;

/**
 * Created by singy on 3/22/2018.
 */

public class ProfileResponse {

    private Profile player;
    private List<Route> routes;

    public Profile getPlayer() {
        return player;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
