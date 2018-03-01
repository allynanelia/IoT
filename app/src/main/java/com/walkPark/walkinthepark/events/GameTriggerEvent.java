package com.walkPark.walkinthepark.events;

import com.walkPark.walkinthepark.models.Route;

/**
 * Created by Boon Sing on 19-Feb-18.
 */

public class GameTriggerEvent {
    private final Route route;

    public GameTriggerEvent(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }
}
