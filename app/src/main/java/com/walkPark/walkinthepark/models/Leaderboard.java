package com.walkPark.walkinthepark.models;

import java.util.List;

/**
 * Created by singy on 3/15/2018.
 */

public class Leaderboard {
    String route_id;
    String route_name;
    List<UserInfo> players;

    public Leaderboard() {
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public List<UserInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserInfo> players) {
        this.players = players;
    }
}
