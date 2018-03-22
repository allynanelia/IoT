package com.walkPark.walkinthepark.models;

/**
 * Created by singy on 3/22/2018.
 */

public class Profile {
    private String name;
    private String email;
    private String profile_pic;
    private String age;
    private String nric;
    private Double weight;
    private Double height;
    private Integer total_steps_taken;
    private Double total_time_taken;
    private Double total_points;
    private Integer total_calories;

    public Profile(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Integer getTotal_steps_taken() {
        return total_steps_taken;
    }

    public void setTotal_steps_taken(int total_steps_taken) {
        this.total_steps_taken = total_steps_taken;
    }

    public Double getTotal_time_taken() {
        return total_time_taken;
    }

    public void setTotal_time_taken(double total_time_taken) {
        this.total_time_taken = total_time_taken;
    }

    public Double getTotal_points() {
        return total_points;
    }

    public void setTotal_points(double total_points) {
        this.total_points = total_points;
    }

    public Integer getTotal_calories() {
        return total_calories;
    }

    public void setTotal_calories(int total_calories) {
        this.total_calories = total_calories;
    }
}
