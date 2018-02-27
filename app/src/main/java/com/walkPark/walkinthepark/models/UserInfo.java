package com.walkPark.walkinthepark.models;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class UserInfo {
    String user_token;
    String user_name;
    String user_photo;
    String user_age;
    String user_step;
    String user_anything;

    public UserInfo() {
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getUser_anything() {
        return user_anything;
    }

    public void setUser_anything(String user_anything) {
        this.user_anything = user_anything;
    }
}
