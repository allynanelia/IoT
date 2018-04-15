package com.walkPark.walkinthepark.models;

/**
 * Created by nanelia on 15/3/18.
 */

public class CheckpointDetails {
    //route_id, checkpoint_id, player_id, timetaken, steps

    String route_id;
    String checkpoint_id;
    String player_id;
    String steps;
    String time_taken;
    String speed;
    boolean has_given_up;

    public CheckpointDetails(String route_id, String checkpoint_id, String player_id, String steps, String time_taken, String speed, boolean has_given_up) {
        this.route_id = route_id;
        this.checkpoint_id = checkpoint_id;
        this.player_id = player_id;
        this.steps = steps;
        this.time_taken = time_taken;
        this.speed = speed;
        this.has_given_up = has_given_up;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getCheckpoint_id() {
        return checkpoint_id;
    }

    public void setCheckpoint_id(String checkpoint_id) {
        this.checkpoint_id = checkpoint_id;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public boolean getHas_given_up() {
        return has_given_up;
    }

    public void setHas_given_up(boolean has_given_up) {
        this.has_given_up = has_given_up;
    }
}
