package com.walkPark.walkinthepark.models;

import android.support.annotation.IntegerRes;

import java.util.List;

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
    private Integer total_points;
    private Integer total_calories_burned;
    private Integer monthly_steps_left;
    private Integer current_month_total_steps;
    private List<WeeklySteps> weekly_steps;


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

    public Integer getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public Integer getTotal_calories() {
        return total_calories_burned;
    }

    public void setTotal_calories(int total_calories) {
        this.total_calories_burned = total_calories;
    }

    public Integer getMonthly_steps_lefts() {
        return monthly_steps_left;
    }

    public void setMonthly_steps_lefts(Integer monthly_steps_lefts) {
        this.monthly_steps_left = monthly_steps_lefts;
    }

    public List<WeeklySteps> getWeekly_steps() {
        return weekly_steps;
    }

    public void setWeekly_steps(List<WeeklySteps> weekly_steps) {
        this.weekly_steps = weekly_steps;
    }

    public Integer getCurrent_month_total_steps() {
        return current_month_total_steps;
    }

    public void setCurrent_month_total_steps(Integer current_month_total_steps) {
        this.current_month_total_steps = current_month_total_steps;
    }
}
