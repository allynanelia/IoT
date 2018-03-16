package com.walkPark.walkinthepark.models;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class UserInfo {
    String player_id;
    String player_name;
    String user_photo;
    String user_age;
    String user_step;
    String points;

    public UserInfo() {
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_step() {
        return user_step;
    }

    public void setUser_step(String user_step) {
        this.user_step = user_step;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
