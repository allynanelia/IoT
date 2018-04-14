package com.walkPark.walkinthepark.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Boon Sing on 16-Feb-18.
 */
@Parcel
public class Route {
    String id;
    String name;
    String image_url;
    String description;
    String estimated_steps;
    List<CheckPoint> checkpoints;
    boolean is_recommended;
    int total_points;
    int total_steps;
    int total_time_taken;

    public Route() {}

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimated_steps() {
        return estimated_steps;
    }

    public void setEstimated_steps(String estimated_steps) {
        this.estimated_steps = estimated_steps;
    }

    public List<CheckPoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getTotal_steps() {
        return total_steps;
    }

    public void setTotal_steps(int total_steps) {
        this.total_steps = total_steps;
    }

    public Integer getTotal_time_taken() {
        return total_time_taken;
    }

    public void setTotal_time_taken(int total_time_taken) {
        this.total_time_taken = total_time_taken;
    }

    public boolean getIs_recommended() {
        return is_recommended;
    }

    public void setIs_recommended(boolean is_recommended) {
        this.is_recommended = is_recommended;
    }
}
